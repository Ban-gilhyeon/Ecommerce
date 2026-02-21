package small.ecommerce.domain.product.dto

import small.ecommerce.domain.enums.Category
import small.ecommerce.domain.enums.Gender
import small.ecommerce.domain.product.Product
import small.ecommerce.domain.product.ProductSize

data class ProductReadOneProductResponse(
    val id: Long,
    val name: String,
    val price: Int,
    val brandName: String,
    val stock: Int,
    val category: Category,
    val gender: Gender,
    val size: ProductSize
) {
    companion object{
        fun from(product: Product, stock: Int) =
            ProductReadOneProductResponse(
                id = product.id,
                name = product.name,
                price = product.price,
                brandName = product.brand.name,
                stock = stock,
                category = product.category,
                gender = product.gender,
                size = product.size
            )
    }
}
