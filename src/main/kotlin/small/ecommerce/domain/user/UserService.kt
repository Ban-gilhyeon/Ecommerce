package small.ecommerce.domain.user

import org.springframework.stereotype.Service
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.exception.UserException

@Service
class UserService(
    private val userRepository: UserRepository
) {
    //command
    fun saveUser(user:User): User {
        return userRepository.save(user)
    }

    //read
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

    //validate
    fun existsUserByEmail(email: String):Boolean{
        return userRepository.existsUserByEmail(email)
    }

    fun userReference(userId: Long): User{
        return userRepository.getReferenceById(userId)
    }
}