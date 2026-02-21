package small.ecommerce.common.logging

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.context.annotation.Profile
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
@Profile("!test")
@Order(Ordered.HIGHEST_PRECEDENCE)
class HttpRequestLoggingFilter : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val requestId = request.getHeader("X-Request-Id")
            ?.takeIf { it.isNotBlank() }
            ?: UUID.randomUUID().toString().replace("-", "").take(16)
        val startedAt = System.currentTimeMillis()

        MDC.put("requestId", requestId)
        response.setHeader("X-Request-Id", requestId)

        log.info(
            "REQUEST START method={}, uri={}, query={}, clientIp={}",
            request.method,
            request.requestURI,
            request.queryString ?: "",
            request.remoteAddr,
        )

        try {
            filterChain.doFilter(request, response)
        } finally {
            val elapsedMs = System.currentTimeMillis() - startedAt
            log.info(
                "REQUEST END method={}, uri={}, status={}, elapsedMs={}",
                request.method,
                request.requestURI,
                response.status,
                elapsedMs,
            )
            MDC.remove("requestId")
        }
    }
}
