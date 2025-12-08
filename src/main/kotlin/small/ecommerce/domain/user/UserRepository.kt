package small.ecommerce.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findUserByEmail(email: String): User?

    fun existsUserByEmail(email: String): Boolean
}