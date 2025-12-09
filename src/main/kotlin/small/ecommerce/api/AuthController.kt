package small.ecommerce.api

import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import small.ecommerce.domain.auth.AuthService
import small.ecommerce.domain.auth.dto.AuthLoginRequest
import small.ecommerce.domain.auth.dto.AuthLoginResponse
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


    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody request: UserSignUpRequest): ResponseEntity<UserSignUpResponse>{
        val response = authService.SignUp(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthLoginRequest): AuthLoginResponse{
        log.info("auth login request")
        return authService.login(request.email, request.password)
    }
}