package small.ecommerce.domain.user.dto

data class UserSignUpResponse(
    val id: Long,
    val email: String,
    val name: String
) {
}