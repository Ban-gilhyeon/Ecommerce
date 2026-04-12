package small.ecommerce.domain.product.dto

import small.ecommerce.domain.enums.Category
import small.ecommerce.domain.enums.Gender
import small.ecommerce.domain.product.ProductSize

sealed class ProductRequest {
    data class CreateProduct(
        val brandId: Long,
        val stockId: Long = 0,
        val name: String,
        val price: Int,
        val category: Category,
        val gender: Gender,
    ) : ProductRequest()

    data class UpdateProduct(
        val brandId: Long? = null,
        val stockId: Long? = null,
        val name: String? = null,
        val price: Int? = null,
        val category: Category? = null,
        val gender: Gender? = null,
    ) : ProductRequest()

    data class CreateProductStock(
        val productId: Long,
        val size: ProductSize,
        val color: String,
        val quantity: Int,
    ) : ProductRequest()

    data class UpdateProductStock(
        val productId: Long? = null,
        val size: ProductSize? = null,
        val color: String? = null,
        val quantity: Int? = null,
    ) : ProductRequest()
}
