package small.ecommerce.domain.product.dto

import small.ecommerce.domain.enums.Category
import small.ecommerce.domain.enums.Gender
import small.ecommerce.domain.product.Product
import small.ecommerce.domain.product.ProductSize

data class ProductReadInfoResponse(
    val id: Long,
    val name: String,
    val size: ProductSize,
    val category: Category,
    val gender: Gender,
    val price : Int,
    val stock: Int,
    val brandId: Long,
) {
    companion object{
        fun from(product: Product, stock: Int): ProductReadInfoResponse{
            return ProductReadInfoResponse(
                id = product.id,
                name = product.name,
                size = product.size,
                category = product.category,
                gender = product.gender,
                price = product.price,
                stock = stock,
                brandId = product.brand.id
            )
        }
    }
}
