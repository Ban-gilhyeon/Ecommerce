package small.ecommerce.domain.user

import org.springframework.stereotype.Service
import small.ecommerce.common.exception.ErrorCode

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun saveUser(user:User): User {
        return userRepository.save(user)
    }

    fun getUserByEmail(email: String): User{
        return userRepository.findUserByEmail(email)
            ?: throw UserException(
                errorCode = ErrorCode.NOT_FOUND_USER_BY_EMAIL,
                detail = mapOf("email" to email)
            )
    }

    fun existsUserByEmail(email: String):Boolean{
        return userRepository.existsUserByEmail(email)
    }
}