package small.ecommerce.auth

import org.slf4j.LoggerFactory
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import small.ecommerce.domain.auth.AuthService
import small.ecommerce.domain.user.UserService
import small.ecommerce.domain.user.dto.UserSignUpRequest
import small.ecommerce.domain.user.dto.UserSignUpResponse

@RestController
@RequestMapping("/api/v1/auth")

class AuthController(
    private val authService: AuthService,
    private val userService: UserService,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)
    data class LoginRequest(val email: String, val password: String)

    @PostMapping("/signup")
    fun signUp(@RequestBody request: UserSignUpRequest): ResponseEntity<UserSignUpResponse>{
        val response = authService.SignUp(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): String{
        log.info("auth login request")
        return authService.login(request.email, request.password)
    }
}