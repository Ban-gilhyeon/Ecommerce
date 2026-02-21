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
    private val brandService: BrandService,
    private val productStockRepo: ProductStockRepository,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    //Create
    fun addProduct(request: ProductAddRequest): ProductAddResponse{
        val brand: Brand = brandService.readBrandById(request.brandId)
        log.info("add product request log ={}", request)

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
        productStockRepo.save(
            ProductStock(
                productId = product.id,
                availableStock = request.stock,
            )
        )
        return ProductAddResponse.of(product, request.stock)
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
        val stock = readStockByProductId(product.id)
        return ProductReadInfoResponse.from(product, stock)

    }

    //해당 브랜드의 모든 product Read
    fun readProductListByBrandId(brandId: Long): List<ProductReadInfoResponse>{
        val products = productRepo.readProductsByBrandId(brandId)
        val stockByProductId = readStockMapByProductIds(products.map { it.id })
        return products.map {
            ProductReadInfoResponse.from(
                product = it,
                stock = stockByProductId[it.id] ?: 0,
            )
        }
    }

    //id 리스트로 상품 리스트 Read
    fun readProductListByProductIdList(productIdList: List<Long>): List<Product>{
        val productList = productRepo.findAllById(productIdList)
        validateProductList(productIdList, productList)
        return productList
    }

    fun productReference(productId: Long): Product {
        return productRepo.getReferenceById(productId)
    }

    fun soldProduct(productId: Long, quantity: Int){
        log.info("stock decrease requested productId={}, quantity={}", productId, quantity)
        val updated = productStockRepo.decreaseStock(productId, quantity)
        validateProductOfStock(updated, productId, quantity)
        log.info("stock decrease completed productId={}, quantity={}", productId, quantity)
    }

    private fun readStockByProductId(productId: Long): Int {
        return productStockRepo.findById(productId)
            .orElseThrow {
                ProductException(
                    errorCode = ErrorCode.PRODUCT_NOT_FOUND_PRODUCT_BY_ID,
                    detail = mapOf("id" to productId),
                    message = "해당 상품의 재고 정보를 찾을 수 없습니다.",
                )
            }
            .availableStock
    }

    private fun readStockMapByProductIds(productIds: List<Long>): Map<Long, Int> {
        if (productIds.isEmpty()) {
            return emptyMap()
        }
        return productStockRepo.findAllByProductIdIn(productIds)
            .associate { it.productId to it.availableStock }
    }

    //상품 재고 확인
    fun validateProductOfStock(updated: Int, productId: Long, quantity: Int){
        if (updated == 0){
            throw ProductException(
                errorCode = ErrorCode.PRODUCT_CONFLICT_OUT_OF_STOCK,
                detail = mapOf(
                    "productId" to productId,
                    "requested" to quantity,
                ),
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
