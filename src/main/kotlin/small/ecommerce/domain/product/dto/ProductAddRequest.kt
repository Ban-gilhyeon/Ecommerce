package small.ecommerce.domain.product.dto

data class ProductAddRequest(
    val name: String,
    val size: String,
    val category: String,
    val gender: String,
    val stock: Int,
    val brandId: Long,
    val price: Int
) {
}