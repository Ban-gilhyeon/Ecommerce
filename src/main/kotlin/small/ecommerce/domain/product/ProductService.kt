package small.ecommerce.domain.product

import org.springframework.stereotype.Service
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.Brand.Brand
import small.ecommerce.domain.Brand.BrandRepository
import small.ecommerce.domain.Brand.BrandService
import small.ecommerce.domain.exception.BrandException
import small.ecommerce.domain.exception.ProductException
import small.ecommerce.domain.product.dto.ProductAddRequest
import small.ecommerce.domain.product.dto.ProductAddResponse

@Service
class ProductService(
    private val productRepo: ProductRepository,
    private val brandService: BrandService
) {
    //Create
    fun addProduct(request: ProductAddRequest): ProductAddResponse{
        val brand: Brand = brandService.readBrandById(request.brandId)
        val product: Product = Product(
            name = request.name,
            price = request.price,
            brand = brand,
            stock = request.stock,
            category = Category.from(request.category),
            gender = Gender.from(request.gender),
            size = ProductSize.from(request.size)
            )
        productRepo.save(product)
        return ProductAddResponse.of(product)
    }

    //Read
    //productId로 하나 Read
    fun readProductByProductId(id: Long): Product {
        return productRepo.findById(id).orElseThrow {
            ProductException(
                errorCode = ErrorCode.PRODUCT_NOT_FOUND_PRODUCT_BY_ID,
                detail = mapOf("id" to id)
            )
        }
    }

    //해당 브랜드의 모든 product Read

}