package small.ecommerce.auth.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import small.ecommerce.auth.UserDetailsService
import small.ecommerce.domain.auth.dto.UserPrincipal

@Component
class JwtAuthFilter(
    private val jwt: JwtTokenProvider

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
            val userPrincipal: UserPrincipal = jwt.getUserPrincipalFromAccessToken(token)

            val authorities = listOf(SimpleGrantedAuthority("${userPrincipal.userRole}"))

            val auth = UsernamePasswordAuthenticationToken(
                userPrincipal,
                null,
                authorities
            )
            SecurityContextHolder.getContext().authentication = auth
        }

        filterChain.doFilter(request,response)
    }
}