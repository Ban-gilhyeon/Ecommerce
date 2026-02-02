package small.ecommerce.domain.auth.dto

import small.ecommerce.domain.user.UserRole

data class UserPrincipal(
    val userId: Long,
    val userRole: UserRole
) {
    companion object{
        fun from(userId: Long, userRole: UserRole): UserPrincipal {
            return UserPrincipal(
                userId = userId,
                userRole = userRole
            )
        }
    }
}