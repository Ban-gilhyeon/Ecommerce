package small.ecommerce.auth

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import small.ecommerce.domain.user.UserRepository

@Service
class UserDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findUserByEmail(email)
            //@Todo 임시 예외 처리
            ?: throw RuntimeException("User Not Found")

        return CustomUserDetails(user)
    }
}