package small.ecommerce.domain.product

import org.springframework.stereotype.Service
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.brand.BrandService
import small.ecommerce.domain.exception.ProductException
import small.ecommerce.domain.product.dto.ProductRequest

@Service
class ProductService(
    private val productRepo: ProductRepository,
    private val productStockRepository: ProductStockRepository,
    private val brandService: BrandService
) {
    fun createProduct(request: ProductRequest.CreateProduct): Product {
        brandService.readBrandById(request.brandId)

        return productRepo.save(
            Product(
                brandId = request.brandId,
                stockId = request.stockId,
                name = request.name,
                price = request.price,
                category = request.category,
                gender = request.gender,
            )
        )
    }

    fun readProducts(): List<Product> {
        return productRepo.findAll()
    }

    fun readProductById(productId: Long): Product {
        return productRepo.findById(productId)
            .orElseThrow {
                ProductException(
                    errorCode = ErrorCode.PRODUCT_NOT_FOUND_PRODUCT_BY_ID,
                    detail = mapOf("productId" to productId)
                )
            }
    }

    fun readProductsByBrandId(brandId: Long): List<Product> {
        brandService.readBrandById(brandId)
        return productRepo.readProductsByBrandId(brandId)
    }

    fun updateProduct(productId: Long, request: ProductRequest.UpdateProduct): Product {
        val product = readProductById(productId)

        request.brandId?.let {
            brandService.readBrandById(it)
            product.brandId = it
        }
        request.stockId?.let { product.stockId = it }
        request.name?.let { product.name = it }
        request.price?.let { product.price = it }
        request.category?.let { product.category = it }
        request.gender?.let { product.gender = it }

        return productRepo.save(product)
    }

    fun deleteProduct(productId: Long) {
        val product = readProductById(productId)
        productRepo.delete(product)
    }

    fun createProductStock(request: ProductRequest.CreateProductStock): ProductStock {
        readProductById(request.productId)

        return productStockRepository.save(
            ProductStock(
                productId = request.productId,
                size = request.size,
                color = request.color,
                quantity = request.quantity,
            )
        )
    }

    fun readProductStocks(): List<ProductStock> {
        return productStockRepository.findAll()
    }

    fun readProductStockById(stockId: Long): ProductStock {
        return productStockRepository.findById(stockId)
            .orElseThrow {
                ProductException(
                    errorCode = ErrorCode.PRODUCT_INVALID_PRODUCT_ID,
                    detail = mapOf("stockId" to stockId),
                    message = "상품 재고를 찾을 수 없습니다."
                )
            }
    }

    fun readProductStocksByProductId(productId: Long): List<ProductStock> {
        readProductById(productId)
        return productStockRepository.findAllByProductId(productId)
    }

    fun updateProductStock(stockId: Long, request: ProductRequest.UpdateProductStock): ProductStock {
        val stock = readProductStockById(stockId)

        request.productId?.let {
            readProductById(it)
            stock.productId = it
        }
        request.size?.let { stock.size = it }
        request.color?.let { stock.color = it }
        request.quantity?.let { stock.quantity = it }

        return productStockRepository.save(stock)
    }

    fun deleteProductStock(stockId: Long) {
        val stock = readProductStockById(stockId)
        productStockRepository.delete(stock)
    }
}
