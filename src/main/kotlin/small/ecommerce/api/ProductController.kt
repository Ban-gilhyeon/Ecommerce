package small.ecommerce.api

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import small.ecommerce.auth.CustomUserDetails
import small.ecommerce.domain.product.ProductService
import small.ecommerce.domain.product.dto.ProductAddRequest
import small.ecommerce.domain.product.dto.ProductAddResponse
import small.ecommerce.domain.product.dto.ProductReadInfoResponse

@RestController
@RequestMapping("/api/v1/product")
class ProductController(
    private val productService: ProductService
)
{
    @PostMapping("{brandId}/add")
    fun addProduct(
        @PathVariable brandId: Long,
        @Valid @RequestBody request: ProductAddRequest,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ):ResponseEntity<ProductAddResponse>{
        return ResponseEntity.ok(productService.addProduct(request))
    }

    @GetMapping("{brandId}/list")
    fun readProductListByBrandId(
        @PathVariable brandId: Long
    ): ResponseEntity<List<ProductReadInfoResponse>>{
        return ResponseEntity.ok(productService.readProductListByBrandId(brandId))
    }
}