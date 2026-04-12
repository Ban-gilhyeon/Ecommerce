package small.ecommerce.domain.user.dto

sealed class UserRequest {
    data class Create(
        val email: String,
        val password: String,
        val name: String,
        val role: String,
        val address: String,
    ) : UserRequest()

    data class Update(
        val email: String? = null,
        val password: String? = null,
        val name: String? = null,
        val role: String? = null,
        val address: String? = null,
    ) : UserRequest()
}
