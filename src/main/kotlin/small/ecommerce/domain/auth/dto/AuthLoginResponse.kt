package small.ecommerce.domain.auth.dto

data class AuthLoginResponse(
    val accessToken: String,
    val email: String
) {
}