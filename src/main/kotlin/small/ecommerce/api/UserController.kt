package small.ecommerce.api

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
class UserController(
    private val userService: UserService,
) {
    @PostMapping
    fun createUser(@RequestBody request: UserRequest.Create): ResponseEntity<UserResponse.Create> {
        return ResponseEntity.ok(UserResponse.Create.from(userService.createUser(request)))
    }

    @GetMapping
    fun readUsers(): ResponseEntity<List<UserResponse.Read>> {
        return ResponseEntity.ok(userService.getUsers().map { UserResponse.Read.from(it) })
    }

    @GetMapping("/{userId}")
    fun readUser(@PathVariable userId: Long): ResponseEntity<UserResponse.Read> {
        return ResponseEntity.ok(UserResponse.Read.from(userService.getUserByUserId(userId)))
    }

    @PutMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: Long,
        @RequestBody request: UserRequest.Update
    ): ResponseEntity<UserResponse.Update> {
        return ResponseEntity.ok(UserResponse.Update.from(userService.updateUser(userId, request)))
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: Long): ResponseEntity<Unit> {
        userService.deleteUser(userId)
        return ResponseEntity.noContent().build()
    }
}
