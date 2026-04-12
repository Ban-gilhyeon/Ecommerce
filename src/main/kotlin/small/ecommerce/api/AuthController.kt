package small.ecommerce.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
import small.ecommerce.domain.user.dto.UserRequest
import small.ecommerce.domain.user.dto.UserResponse

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Authentication API")

class AuthController(
    private val authService: AuthService,
    private val userService: UserService,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)


    @PostMapping("/signup")
    @Operation(summary = "Sign up")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User signed up"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
        ],
    )
    fun signUp(@Valid @RequestBody request: UserRequest.Create): ResponseEntity<UserResponse.Create>{
        val response = authService.SignUp(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    @Operation(summary = "Log in")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Logged in"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "401", description = "Invalid credentials"),
        ],
    )
    fun login(@RequestBody request: AuthLoginRequest): AuthLoginResponse{
        log.info("auth login request")
        return authService.login(request.email, request.password)
    }
}
