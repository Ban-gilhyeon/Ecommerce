package small.ecommerce.domain.product.dto

import small.ecommerce.domain.product.Category
import small.ecommerce.domain.product.Gender
import small.ecommerce.domain.product.Product
import small.ecommerce.domain.product.ProductSize

data class ProductAddResponse(
    val name: String,
    val size: ProductSize,
    val category: Category,
    val gender: Gender,
    val stock: Int,
    val price: Int,
    val brandId: Long
) {
    companion object{
        fun of(product: Product): ProductAddResponse =
            ProductAddResponse(
                name = product.name,
                price = product.price,
                brandId = product.brand.id,
                stock = product.stock,
                size = product.size,
                category = product.category,
                gender = product.gender
            )
    }
}