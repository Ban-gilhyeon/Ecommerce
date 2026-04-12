package small.ecommerce.domain.product.dto

import small.ecommerce.domain.product.Product
import small.ecommerce.domain.product.ProductStock

sealed class ProductResponse {
    data class CreateProduct(
        val brandId: Long,
        val productId: Long,
        val stockId: Long,
        val name: String,
        val price: Int,
        val category: String,
        val gender: String,
    ) : ProductResponse() {
        companion object {
            fun from(product: Product): CreateProduct =
                CreateProduct(
                    brandId = product.brandId,
                    productId = product.id,
                    stockId = product.stockId,
                    name = product.name,
                    price = product.price,
                    category = product.category.name,
                    gender = product.gender.name,
                )
        }
    }

    data class ReadProduct(
        val brandId: Long,
        val productId: Long,
        val stockId: Long,
        val name: String,
        val price: Int,
        val category: String,
        val gender: String,
    ) : ProductResponse() {
        companion object {
            fun from(product: Product): ReadProduct =
                ReadProduct(
                    brandId = product.brandId,
                    productId = product.id,
                    stockId = product.stockId,
                    name = product.name,
                    price = product.price,
                    category = product.category.name,
                    gender = product.gender.name,
                )
        }
    }

    data class UpdateProduct(
        val brandId: Long,
        val productId: Long,
        val stockId: Long,
        val name: String,
        val price: Int,
        val category: String,
        val gender: String,
    ) : ProductResponse() {
        companion object {
            fun from(product: Product): UpdateProduct =
                UpdateProduct(
                    brandId = product.brandId,
                    productId = product.id,
                    stockId = product.stockId,
                    name = product.name,
                    price = product.price,
                    category = product.category.name,
                    gender = product.gender.name,
                )
        }
    }

    data class DeleteProduct(
        val productId: Long,
    ) : ProductResponse()

    data class CreateProductStock(
        val id: Long,
        val productId: Long,
        val quantity: Int,
        val productSize: String,
        val color: String,
    ) : ProductResponse() {
        companion object {
            fun from(stock: ProductStock): CreateProductStock =
                CreateProductStock(
                    id = stock.id,
                    productId = stock.productId,
                    quantity = stock.quantity,
                    productSize = stock.size.name,
                    color = stock.color,
                )
        }
    }

    data class ReadProductStock(
        val id: Long,
        val productId: Long,
        val quantity: Int,
        val productSize: String,
        val color: String,
    ) : ProductResponse() {
        companion object {
            fun from(stock: ProductStock): ReadProductStock =
                ReadProductStock(
                    id = stock.id,
                    productId = stock.productId,
                    quantity = stock.quantity,
                    productSize = stock.size.name,
                    color = stock.color,
                )
        }
    }

    data class UpdateProductStock(
        val id: Long,
        val productId: Long,
        val quantity: Int,
        val productSize: String,
        val color: String,
    ) : ProductResponse() {
        companion object {
            fun from(stock: ProductStock): UpdateProductStock =
                UpdateProductStock(
                    id = stock.id,
                    productId = stock.productId,
                    quantity = stock.quantity,
                    productSize = stock.size.name,
                    color = stock.color,
                )
        }
    }

    data class DeleteProductStock(
        val id: Long,
    ) : ProductResponse()
}
