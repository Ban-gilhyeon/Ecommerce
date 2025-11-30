package small.ecommerce.auth

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")

class AuthController(
    private val authService: AuthService,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)
    data class LoginRequest(val email: String, val password: String)

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): String{
        log.info("auth login request")
        return authService.login(request.email, request.password)
    }
}