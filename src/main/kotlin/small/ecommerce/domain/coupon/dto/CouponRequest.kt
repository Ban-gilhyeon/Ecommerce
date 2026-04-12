package small.ecommerce.domain.coupon.dto

import small.ecommerce.domain.coupon.DiscountType
import small.ecommerce.domain.enums.Category
import java.time.LocalDateTime

sealed class CouponRequest {
    data class CreateCoupon(
        val name: String,
        val category: Category,
        val expirationTime: LocalDateTime,
        val discountType: DiscountType,
        val discountValue: Long,
    ) : CouponRequest()

    data class UpdateCoupon(
        val name: String? = null,
        val category: Category? = null,
        val expirationTime: LocalDateTime? = null,
        val discountType: DiscountType? = null,
        val discountValue: Long? = null,
    ) : CouponRequest()

    data class CreateCouponIssue(
        val userId: Long,
        val couponId: Long,
    ) : CouponRequest()

    data class UpdateCouponIssue(
        val userId: Long? = null,
        val couponId: Long? = null,
    ) : CouponRequest()
}
