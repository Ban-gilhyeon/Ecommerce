package small.ecommerce.auth

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.exception.UserException
import small.ecommerce.domain.user.UserRepository

@Service
class UserDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findUserByEmail(email)
            ?: throw UserException(
                errorCode = ErrorCode.NOT_FOUND_USER_BY_EMAIL,
                detail = mapOf("email" to email)
            )

        return CustomUserDetails(user)
    }

    fun loadUserById(id: Long): UserDetails{
        val user = userRepository.findUserById(id)
            ?: throw UserException(
                errorCode = ErrorCode.NOT_FOUND_USER_BY_ID,
                detail = mapOf("id" to id)
            )

        return CustomUserDetails(user)
    }
}