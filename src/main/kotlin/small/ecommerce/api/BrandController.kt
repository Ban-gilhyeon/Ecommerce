package small.ecommerce.api

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import small.ecommerce.auth.CustomUserDetails
import small.ecommerce.domain.Brand.BrandService
import small.ecommerce.domain.Brand.dto.BrandAddRequest
import small.ecommerce.domain.Brand.dto.BrandAddResponse
import small.ecommerce.domain.Brand.dto.BrandInfoResponse

@RestController
@RequestMapping("/api/v1/brand")
class BrandController(
    private val brandService: BrandService
) {

    //create
    @PostMapping("/add")
    fun addBrand(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody request: BrandAddRequest
    ): ResponseEntity<BrandAddResponse>{
        return ResponseEntity.ok(BrandAddResponse.of(brandService.addBrand(request)))
    }

    //read
    @GetMapping("/list")
    fun readBrandInfoList(
    ): ResponseEntity<List<BrandInfoResponse>>{
        return ResponseEntity.ok(brandService.readAllBrandInfoList())
    }

    //update

    //delete


}