package small.ecommerce.domain.auth.dto

data class AuthLoginRequest(
    val email: String,
    val password: String,

) {
}