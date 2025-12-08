package small.ecommerce.api

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import small.ecommerce.domain.user.UserService

@RequestMapping("api/v1/users/")
class UserController(
    private val userService: UserService,
) {

}