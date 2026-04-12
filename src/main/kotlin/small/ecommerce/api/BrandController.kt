package small.ecommerce.api

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import small.ecommerce.auth.CustomUserDetails
import small.ecommerce.domain.brand.BrandService
import small.ecommerce.domain.brand.dto.BrandRequest
import small.ecommerce.domain.brand.dto.BrandResponse


@RestController
@RequestMapping("/api/v1/brand")
class BrandController(
    private val brandService: BrandService
) {

    //create
    @PostMapping("/add")
    fun addBrand(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody request: BrandRequest.Create
    ): ResponseEntity<BrandResponse.Create>{
        return ResponseEntity.ok(BrandResponse.Create.from(brandService.addBrand(request)))
    }

    //read
    @GetMapping("/list")
    fun readBrandInfoList(
    ): ResponseEntity<List<BrandResponse.Read>>{
        return ResponseEntity.ok(brandService.readAllBrands().map { BrandResponse.Read.from(it) })
    }

    @GetMapping("/{brandId}")
    fun readBrandInfo(
        @PathVariable brandId: Long
    ): ResponseEntity<BrandResponse.Read> {
        return ResponseEntity.ok(BrandResponse.Read.from(brandService.readBrandById(brandId)))
    }

    //update
    @PutMapping("/{brandId}")
    fun updateBrand(
        @PathVariable brandId: Long,
        @RequestBody request: BrandRequest.Update
    ): ResponseEntity<BrandResponse.Update> {
        return ResponseEntity.ok(BrandResponse.Update.from(brandService.updateBrand(brandId, request)))
    }

    //delete
    @DeleteMapping("/{brandId}")
    fun deleteBrand(
        @PathVariable brandId: Long
    ): ResponseEntity<Unit> {
        brandService.deleteBrand(brandId)
        return ResponseEntity.noContent().build()
    }


}
