package small.ecommerce.domain.auth

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import small.ecommerce.auth.jwt.JwtTokenProvider
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.auth.dto.AuthLoginResponse
import small.ecommerce.domain.user.User
import small.ecommerce.domain.exception.UserException
import small.ecommerce.domain.user.UserRole
import small.ecommerce.domain.user.UserService
import small.ecommerce.domain.user.dto.UserSignUpRequest
import small.ecommerce.domain.user.dto.UserSignUpResponse

@Service
class AuthService(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun SignUp(request: UserSignUpRequest): UserSignUpResponse{
        //이메일 중복 체크
        if(userService.existsUserByEmail(request.email)){
            throw UserException(
                errorCode = ErrorCode.USER_ALREADY_EXISTS,
                detail = mapOf("email" to request.email)
            )
        }

        //비밀번호 암호화
        val encodedPassword = passwordEncoder.encode(request.password)

        //엔티티 생성 & 저장
        val user = User(
            email = request.email,
            password = encodedPassword.toString(),
            name = request.name,
            role = UserRole.valueOf(request.role),
            address = request.address
        )
        userService.saveUser(user)

        //응답 반환
        return UserSignUpResponse(
            id = user.id,
            email = user.email,
            name = user.name,
            address = user.address
        )
    }

    fun login(email: String, password: String): AuthLoginResponse{
        val authenticationToken = UsernamePasswordAuthenticationToken(email, password)
        authenticationManager.authenticate(authenticationToken)

        //로그인 성공 시 JWT 발급
        val user = userService.getUserByEmail(email)
        val accessToken: String = jwtTokenProvider.generateAccessToken(user.id, user.role)
        val response: AuthLoginResponse = AuthLoginResponse(
            accessToken = "Bearer "+ accessToken,
            email = user.email
        )
        return response
    }
}