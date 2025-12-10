package small.ecommerce.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import small.ecommerce.domain.Brand.dto.BrandAddResponse
import small.ecommerce.domain.product.ProductService
import small.ecommerce.domain.product.dto.ProductAddRequest

@RestController
@RequestMapping("/api/v1/product/{brandId}")
class ProductController(
    private val productService: ProductService
)
{
    @PostMapping("/add")
    fun addProduct(@RequestBody request: ProductAddRequest):ResponseEntity<BrandAddResponse>{
        return ResponseEntity.ok(productService.addProduct(request))
    }
}