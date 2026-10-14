package small.ecommerce.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
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
import small.ecommerce.common.response.ApiResponse as CommonApiResponse
import small.ecommerce.domain.brand.BrandService
import small.ecommerce.domain.brand.dto.BrandRequest
import small.ecommerce.domain.brand.dto.BrandResponse


@RestController
@RequestMapping("/api/v1/brand")
@Tag(name = "Brand", description = "Brand API")
class BrandController(
    private val brandService: BrandService
) {

    //create
    @PostMapping("/add")
    @Operation(
        summary = "Create brand",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Brand created"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "403", description = "Seller role required"),
        ],
    )
    fun addBrand(
        @Parameter(hidden = true)
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody request: BrandRequest.Create
    ): ResponseEntity<CommonApiResponse<BrandResponse.Create>>{
        return ResponseEntity.ok(CommonApiResponse.success(BrandResponse.Create.from(brandService.addBrand(request))))
    }

    //read
    @GetMapping("/list")
    @Operation(summary = "List brands")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Brands returned"),
        ],
    )
    fun readBrandInfoList(
    ): ResponseEntity<CommonApiResponse<BrandResponse.ReadList>>{
        return ResponseEntity.ok(CommonApiResponse.success(BrandResponse.ReadList.from(brandService.readAllBrands())))
    }

    @GetMapping("/{brandId}")
    @Operation(
        summary = "Get brand",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Brand returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Brand not found"),
        ],
    )
    fun readBrandInfo(
        @Parameter(description = "Brand ID")
        @PathVariable brandId: Long
    ): ResponseEntity<CommonApiResponse<BrandResponse.Read>> {
        return ResponseEntity.ok(CommonApiResponse.success(BrandResponse.Read.from(brandService.readBrandById(brandId))))
    }

    //update
    @PutMapping("/{brandId}")
    @Operation(
        summary = "Update brand",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Brand updated"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Brand not found"),
        ],
    )
    fun updateBrand(
        @Parameter(description = "Brand ID")
        @PathVariable brandId: Long,
        @RequestBody request: BrandRequest.Update
    ): ResponseEntity<CommonApiResponse<BrandResponse.Update>> {
        return ResponseEntity.ok(CommonApiResponse.success(BrandResponse.Update.from(brandService.updateBrand(brandId, request))))
    }

    //delete
    @DeleteMapping("/{brandId}")
    @Operation(
        summary = "Delete brand",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Brand deleted"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Brand not found"),
        ],
    )
    fun deleteBrand(
        @Parameter(description = "Brand ID")
        @PathVariable brandId: Long
    ): ResponseEntity<CommonApiResponse<BrandResponse.Delete>> {
        brandService.deleteBrand(brandId)
        return ResponseEntity.ok(CommonApiResponse.success(BrandResponse.Delete.from(brandId)))
    }


}
