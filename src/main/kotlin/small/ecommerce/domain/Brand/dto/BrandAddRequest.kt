package small.ecommerce.domain.Brand.dto

data class BrandAddRequest(
    val name: String,
    val description: String,
    val owner: Long
) {
}