package small.ecommerce.domain.user.dto

data class UserSignUpRequest(
    val email: String,
    val password: String,
    val name: String,

) {
}