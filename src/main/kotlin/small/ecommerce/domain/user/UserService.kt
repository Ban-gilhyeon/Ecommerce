package small.ecommerce.domain.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.exception.UserException
import small.ecommerce.domain.user.dto.UserRequest

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    //command
    fun saveUser(user:User): User {
        return userRepository.save(user)
    }

    fun createUser(request: UserRequest.Create): User {
        if (existsUserByEmail(request.email)) {
            throw UserException(
                errorCode = ErrorCode.USER_ALREADY_EXISTS,
                detail = mapOf("email" to request.email)
            )
        }

        return userRepository.save(
            User(
                email = request.email,
                password = requireNotNull(passwordEncoder.encode(request.password)),
                name = request.name,
                role = UserRole.valueOf(request.role.uppercase()),
                address = request.address,
            )
        )
    }

    //read
    fun getUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserByEmail(email: String): User{
        return userRepository.findUserByEmail(email)
            ?: throw UserException(
                errorCode = ErrorCode.NOT_FOUND_USER_BY_EMAIL,
                detail = mapOf("email" to email)
            )
    }

    fun getUserByUserId(userId: Long): User{
        return userRepository.findUserById(userId)
            ?:throw UserException(
                errorCode = ErrorCode.NOT_FOUND_USER_BY_ID,
                detail = mapOf("userId" to userId)
            )
    }

    fun updateUser(userId: Long, request: UserRequest.Update): User {
        val user = getUserByUserId(userId)

        request.email?.let {
            if (it != user.email && existsUserByEmail(it)) {
                throw UserException(
                    errorCode = ErrorCode.USER_ALREADY_EXISTS,
                    detail = mapOf("email" to it)
                )
            }
            user.email = it
        }
        request.password?.let { user.password = requireNotNull(passwordEncoder.encode(it)) }
        request.name?.let { user.name = it }
        request.role?.let { user.role = UserRole.valueOf(it.uppercase()) }
        request.address?.let { user.address = it }

        return userRepository.save(user)
    }

    fun deleteUser(userId: Long) {
        val user = getUserByUserId(userId)
        userRepository.delete(user)
    }

    //validate
    fun existsUserByEmail(email: String):Boolean{
        return userRepository.existsUserByEmail(email)
    }
}
