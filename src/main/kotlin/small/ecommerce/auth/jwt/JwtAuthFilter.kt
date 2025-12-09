package small.ecommerce.auth.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import small.ecommerce.auth.UserDetailsService

@Component
class JwtAuthFilter(
    private val jwt: JwtTokenProvider,
    private val userDetailService: UserDetailsService
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substring(7)

        if(token != null && jwt.validateAccessToken(token)){
            val userId = jwt.getUserIdFromAccessToken(token)

            val userDetails = userDetailService.loadUserById(userId)

            val auth = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            )
            SecurityContextHolder.getContext().authentication = auth
        }

        filterChain.doFilter(request,response)
    }
}