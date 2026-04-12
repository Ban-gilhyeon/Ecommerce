package small.ecommerce.api

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
class CouponController(
    private val couponService: CouponService
) {
    @PostMapping
    fun createCoupon(
        @RequestBody request: CouponRequest.CreateCoupon
    ): ResponseEntity<CouponResponse.CreateCoupon> {
        return ResponseEntity.ok(CouponResponse.CreateCoupon.from(couponService.createCoupon(request)))
    }

    @GetMapping
    fun readCoupons(): ResponseEntity<List<CouponResponse.ReadCoupon>> {
        return ResponseEntity.ok(couponService.readCoupons().map { CouponResponse.ReadCoupon.from(it) })
    }

    @GetMapping("/{couponId}")
    fun readCoupon(
        @PathVariable couponId: Long
    ): ResponseEntity<CouponResponse.ReadCoupon> {
        return ResponseEntity.ok(CouponResponse.ReadCoupon.from(couponService.readCouponById(couponId)))
    }

    @PutMapping("/{couponId}")
    fun updateCoupon(
        @PathVariable couponId: Long,
        @RequestBody request: CouponRequest.UpdateCoupon
    ): ResponseEntity<CouponResponse.UpdateCoupon> {
        return ResponseEntity.ok(CouponResponse.UpdateCoupon.from(couponService.updateCoupon(couponId, request)))
    }

    @DeleteMapping("/{couponId}")
    fun deleteCoupon(
        @PathVariable couponId: Long
    ): ResponseEntity<Unit> {
        couponService.deleteCoupon(couponId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/issues")
    fun createCouponIssue(
        @RequestBody request: CouponRequest.CreateCouponIssue
    ): ResponseEntity<CouponResponse.CreateCouponIssue> {
        return ResponseEntity.ok(CouponResponse.CreateCouponIssue.from(couponService.createCouponIssue(request)))
    }

    @GetMapping("/issues")
    fun readCouponIssues(): ResponseEntity<List<CouponResponse.ReadCouponIssue>> {
        return ResponseEntity.ok(couponService.readCouponIssues().map { CouponResponse.ReadCouponIssue.from(it) })
    }

    @GetMapping("/issues/{couponIssueId}")
    fun readCouponIssue(
        @PathVariable couponIssueId: Long
    ): ResponseEntity<CouponResponse.ReadCouponIssue> {
        return ResponseEntity.ok(CouponResponse.ReadCouponIssue.from(couponService.readCouponIssueById(couponIssueId)))
    }

    @PutMapping("/issues/{couponIssueId}")
    fun updateCouponIssue(
        @PathVariable couponIssueId: Long,
        @RequestBody request: CouponRequest.UpdateCouponIssue
    ): ResponseEntity<CouponResponse.UpdateCouponIssue> {
        return ResponseEntity.ok(CouponResponse.UpdateCouponIssue.from(couponService.updateCouponIssue(couponIssueId, request)))
    }

    @DeleteMapping("/issues/{couponIssueId}")
    fun deleteCouponIssue(
        @PathVariable couponIssueId: Long
    ): ResponseEntity<Unit> {
        couponService.deleteCouponIssue(couponIssueId)
        return ResponseEntity.noContent().build()
    }
}
