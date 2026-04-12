package small.ecommerce.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import small.ecommerce.domain.user.UserService
import small.ecommerce.domain.user.dto.UserRequest
import small.ecommerce.domain.user.dto.UserResponse

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "User API")
@SecurityRequirement(name = "bearerAuth")
class UserController(
    private val userService: UserService,
) {
    @PostMapping
    @Operation(summary = "Create user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User created"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
        ],
    )
    fun createUser(@RequestBody request: UserRequest.Create): ResponseEntity<UserResponse.Create> {
        return ResponseEntity.ok(UserResponse.Create.from(userService.createUser(request)))
    }

    @GetMapping
    @Operation(summary = "List users")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Users returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
        ],
    )
    fun readUsers(): ResponseEntity<List<UserResponse.Read>> {
        return ResponseEntity.ok(userService.getUsers().map { UserResponse.Read.from(it) })
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "User not found"),
        ],
    )
    fun readUser(
        @Parameter(description = "User ID")
        @PathVariable userId: Long
    ): ResponseEntity<UserResponse.Read> {
        return ResponseEntity.ok(UserResponse.Read.from(userService.getUserByUserId(userId)))
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User updated"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "User not found"),
        ],
    )
    fun updateUser(
        @Parameter(description = "User ID")
        @PathVariable userId: Long,
        @RequestBody request: UserRequest.Update
    ): ResponseEntity<UserResponse.Update> {
        return ResponseEntity.ok(UserResponse.Update.from(userService.updateUser(userId, request)))
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "User deleted"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "User not found"),
        ],
    )
    fun deleteUser(
        @Parameter(description = "User ID")
        @PathVariable userId: Long
    ): ResponseEntity<Unit> {
        userService.deleteUser(userId)
        return ResponseEntity.noContent().build()
    }
}
