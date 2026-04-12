package small.ecommerce.domain.coupon

import org.springframework.stereotype.Service
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.coupon.dto.CouponRequest
import small.ecommerce.domain.user.UserService

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val couponIssueRepository: CouponIssueRepository,
    private val userService: UserService,
) {
    fun createCoupon(request: CouponRequest.CreateCoupon): Coupon {
        return couponRepository.save(
            Coupon(
                name = request.name,
                category = request.category,
                expirationTime = request.expirationTime,
                discountType = request.discountType,
                discountValue = request.discountValue,
            )
        )
    }

    fun readCoupons(): List<Coupon> {
        return couponRepository.findAll()
    }

    fun readCouponById(couponId: Long): Coupon {
        return couponRepository.findById(couponId)
            .orElseThrow {
                IllegalArgumentException("${ErrorCode.INVALID_REQUEST.message} couponId=$couponId")
            }
    }

    fun updateCoupon(couponId: Long, request: CouponRequest.UpdateCoupon): Coupon {
        val coupon = readCouponById(couponId)

        request.name?.let { coupon.name = it }
        request.category?.let { coupon.category = it }
        request.expirationTime?.let { coupon.expirationTime = it }
        request.discountType?.let { coupon.discountType = it }
        request.discountValue?.let { coupon.discountValue = it }

        return couponRepository.save(coupon)
    }

    fun deleteCoupon(couponId: Long) {
        val coupon = readCouponById(couponId)
        couponRepository.delete(coupon)
    }

    fun createCouponIssue(request: CouponRequest.CreateCouponIssue): CouponIssue {
        val user = userService.getUserByUserId(request.userId)
        val coupon = readCouponById(request.couponId)

        return couponIssueRepository.save(
            CouponIssue(
                user = user,
                coupon = coupon,
            )
        )
    }

    fun readCouponIssues(): List<CouponIssue> {
        return couponIssueRepository.findAll()
    }

    fun readCouponIssueById(couponIssueId: Long): CouponIssue {
        return couponIssueRepository.findById(couponIssueId)
            .orElseThrow {
                IllegalArgumentException("${ErrorCode.INVALID_REQUEST.message} couponIssueId=$couponIssueId")
            }
    }

    fun updateCouponIssue(couponIssueId: Long, request: CouponRequest.UpdateCouponIssue): CouponIssue {
        val couponIssue = readCouponIssueById(couponIssueId)

        request.userId?.let { couponIssue.user = userService.getUserByUserId(it) }
        request.couponId?.let { couponIssue.coupon = readCouponById(it) }

        return couponIssueRepository.save(couponIssue)
    }

    fun deleteCouponIssue(couponIssueId: Long) {
        val couponIssue = readCouponIssueById(couponIssueId)
        couponIssueRepository.delete(couponIssue)
    }
}
