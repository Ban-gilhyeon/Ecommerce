package small.ecommerce.auth

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import small.ecommerce.auth.exception.AuthException
import small.ecommerce.auth.jwt.JwtTokenProvider
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.user.UserRepository

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun login(email: String, password: String): String{
        val authenticationToken = UsernamePasswordAuthenticationToken(email, password)
        authenticationManager.authenticate(authenticationToken)

        //로그인 성공 시 JWT 발급
        val user = userRepository.findUserByEmail(email) ?: throw AuthException(ErrorCode.NOT_FOUND_USER, mapOf("email" to email))
        return jwtTokenProvider.generateAccessToken(user.id)
    }
}