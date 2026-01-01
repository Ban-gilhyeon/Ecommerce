package small.ecommerce.domain.product

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.Brand.Brand
import small.ecommerce.domain.Brand.BrandService
import small.ecommerce.domain.enums.Category
import small.ecommerce.domain.enums.Gender
import small.ecommerce.domain.exception.ProductException
import small.ecommerce.domain.order.dto.ItemInfo
import small.ecommerce.domain.product.dto.ProductAddRequest
import small.ecommerce.domain.product.dto.ProductAddResponse
import small.ecommerce.domain.product.dto.ProductReadInfoResponse

@Service
class ProductService(
    private val productRepo: ProductRepository,
    private val brandService: BrandService
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    //Create
    fun addProduct(request: ProductAddRequest): ProductAddResponse{
        val brand: Brand = brandService.readBrandById(request.brandId)
        log.info(request.name)
        log.info(request.size)
        log.info(request.category)
        log.info(request.gender)
        log.info(request.stock.toString())
        log.info(request.brandId.toString())
        log.info(request.price.toString())

        val product: Product = Product(
            name = request.name,
            price = request.price,
            brand = brand,
            stock = request.stock,
            category = Category.from(request.category),
            gender = Gender.from(request.gender),
            size = ProductSize.from(request.size)
            )
        log.info("domain: product create")
        productRepo.save(product)
        return ProductAddResponse.of(product)
    }

    //Read
    //productId로 하나 Read
    fun readProductByProductId(id: Long): ProductReadInfoResponse {
        val  product = productRepo.findById(id).orElseThrow {
            ProductException(
                errorCode = ErrorCode.PRODUCT_NOT_FOUND_PRODUCT_BY_ID,
                detail = mapOf("id" to id)
            )
        }
        return ProductReadInfoResponse.from(product)

    }

    //해당 브랜드의 모든 product Read
    fun readProductListByBrandId(brandId: Long): List<ProductReadInfoResponse>{
        return productRepo.readProductsByBrandId(brandId)
            .map { ProductReadInfoResponse.from(it) }
    }

    //id 리스트로 상품 리스트 Read
    fun readProductListByProductIdList(productIdList: List<Long>): List<Product>{
        val productList = productRepo.findAllById(productIdList)
        validateProductList(productIdList, productList)
        return productList
    }

    fun soldProduct(product: Product, quantity: Int){
        val updated = productRepo.decreaseStock(product.id, quantity)
        validateProductOfStock(updated, product)
    }

    //상품 재고 확인
    fun validateProductOfStock(updated: Int, product: Product){
        if (updated == 0){
            throw ProductException(
                errorCode = ErrorCode.PRODUCT_CONFLICT_OUT_OF_STOCK,
                detail = mapOf("productId" to product.id,
                    "requested" to quantity,
                    "available" to product.stock),
                message = "해당 상품의 재고가 부족합니다."
            )
        }
    }

    //상품 리스트 확인
    fun validateProductList(requestIds: List<Long>, products: List<Product>){
        val foundIds = products.mapNotNull { it.id }.toSet()
        val missingIds = requestIds - foundIds
        log.info("missingIds : ", missingIds.toString())

        if(missingIds.isNotEmpty()){
            throw ProductException(
                errorCode = ErrorCode.PRODUCT_CONFLICT_OUT_OF_STOCK,
                detail = mapOf("missingProductIds" to missingIds)
            )
        }
    }

}