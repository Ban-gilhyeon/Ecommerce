package small.ecommerce.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import small.ecommerce.domain.coupon.CouponService
import small.ecommerce.domain.coupon.dto.CouponRequest
import small.ecommerce.domain.coupon.dto.CouponResponse

@RestController
@RequestMapping("/api/v1/coupons")
@Tag(name = "Coupon", description = "Coupon API")
@SecurityRequirement(name = "bearerAuth")
class CouponController(
    private val couponService: CouponService
) {
    @PostMapping
    @Operation(summary = "Create coupon")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Coupon created"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
        ],
    )
    fun createCoupon(
        @RequestBody request: CouponRequest.CreateCoupon
    ): ResponseEntity<CouponResponse.CreateCoupon> {
        return ResponseEntity.ok(CouponResponse.CreateCoupon.from(couponService.createCoupon(request)))
    }

    @GetMapping
    @Operation(summary = "List coupons")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Coupons returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
        ],
    )
    fun readCoupons(): ResponseEntity<List<CouponResponse.ReadCoupon>> {
        return ResponseEntity.ok(couponService.readCoupons().map { CouponResponse.ReadCoupon.from(it) })
    }

    @GetMapping("/{couponId}")
    @Operation(summary = "Get coupon")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Coupon returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Coupon not found"),
        ],
    )
    fun readCoupon(
        @Parameter(description = "Coupon ID")
        @PathVariable couponId: Long
    ): ResponseEntity<CouponResponse.ReadCoupon> {
        return ResponseEntity.ok(CouponResponse.ReadCoupon.from(couponService.readCouponById(couponId)))
    }

    @PutMapping("/{couponId}")
    @Operation(summary = "Update coupon")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Coupon updated"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Coupon not found"),
        ],
    )
    fun updateCoupon(
        @Parameter(description = "Coupon ID")
        @PathVariable couponId: Long,
        @RequestBody request: CouponRequest.UpdateCoupon
    ): ResponseEntity<CouponResponse.UpdateCoupon> {
        return ResponseEntity.ok(CouponResponse.UpdateCoupon.from(couponService.updateCoupon(couponId, request)))
    }

    @DeleteMapping("/{couponId}")
    @Operation(summary = "Delete coupon")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Coupon deleted"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Coupon not found"),
        ],
    )
    fun deleteCoupon(
        @Parameter(description = "Coupon ID")
        @PathVariable couponId: Long
    ): ResponseEntity<Unit> {
        couponService.deleteCoupon(couponId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/issues")
    @Operation(summary = "Issue coupon")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Coupon issued"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
        ],
    )
    fun createCouponIssue(
        @RequestBody request: CouponRequest.CreateCouponIssue
    ): ResponseEntity<CouponResponse.CreateCouponIssue> {
        return ResponseEntity.ok(CouponResponse.CreateCouponIssue.from(couponService.createCouponIssue(request)))
    }

    @GetMapping("/issues")
    @Operation(summary = "List coupon issues")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Coupon issues returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
        ],
    )
    fun readCouponIssues(): ResponseEntity<List<CouponResponse.ReadCouponIssue>> {
        return ResponseEntity.ok(couponService.readCouponIssues().map { CouponResponse.ReadCouponIssue.from(it) })
    }

    @GetMapping("/issues/{couponIssueId}")
    @Operation(summary = "Get coupon issue")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Coupon issue returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Coupon issue not found"),
        ],
    )
    fun readCouponIssue(
        @Parameter(description = "Coupon issue ID")
        @PathVariable couponIssueId: Long
    ): ResponseEntity<CouponResponse.ReadCouponIssue> {
        return ResponseEntity.ok(CouponResponse.ReadCouponIssue.from(couponService.readCouponIssueById(couponIssueId)))
    }

    @PutMapping("/issues/{couponIssueId}")
    @Operation(summary = "Update coupon issue")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Coupon issue updated"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Coupon issue not found"),
        ],
    )
    fun updateCouponIssue(
        @Parameter(description = "Coupon issue ID")
        @PathVariable couponIssueId: Long,
        @RequestBody request: CouponRequest.UpdateCouponIssue
    ): ResponseEntity<CouponResponse.UpdateCouponIssue> {
        return ResponseEntity.ok(CouponResponse.UpdateCouponIssue.from(couponService.updateCouponIssue(couponIssueId, request)))
    }

    @DeleteMapping("/issues/{couponIssueId}")
    @Operation(summary = "Delete coupon issue")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Coupon issue deleted"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Coupon issue not found"),
        ],
    )
    fun deleteCouponIssue(
        @Parameter(description = "Coupon issue ID")
        @PathVariable couponIssueId: Long
    ): ResponseEntity<Unit> {
        couponService.deleteCouponIssue(couponIssueId)
        return ResponseEntity.noContent().build()
    }
}
