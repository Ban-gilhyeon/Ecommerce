package small.ecommerce.domain.auth

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import small.ecommerce.auth.jwt.JwtTokenProvider
import small.ecommerce.domain.auth.dto.AuthLoginResponse
import small.ecommerce.domain.user.UserService
import small.ecommerce.domain.user.dto.UserRequest
import small.ecommerce.domain.user.dto.UserResponse

@Service
class AuthService(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun SignUp(request: UserRequest.Create): UserResponse.Create {
        return UserResponse.Create.from(userService.createUser(request))
    }

    fun login(email: String, password: String): AuthLoginResponse{
        val authenticationToken = UsernamePasswordAuthenticationToken(email, password)
        authenticationManager.authenticate(authenticationToken)

        //로그인 성공 시 JWT 발급
        val user = userService.getUserByEmail(email)
        val accessToken: String = jwtTokenProvider.generateAccessToken(user.id)
        val response: AuthLoginResponse = AuthLoginResponse(
            accessToken = "Bearer "+ accessToken,
            email = user.email
        )
        return response
    }
}
