package small.ecommerce.domain.coupon.dto

import small.ecommerce.domain.coupon.Coupon
import small.ecommerce.domain.coupon.CouponIssue
import java.time.LocalDateTime

sealed class CouponResponse {
    data class CreateCoupon(
        val id: Long,
        val name: String,
        val category: String,
        val expirationTime: LocalDateTime,
        val discountType: String,
        val discountValue: Long,
    ) : CouponResponse() {
        companion object {
            fun from(coupon: Coupon): CreateCoupon =
                CreateCoupon(
                    id = coupon.id,
                    name = coupon.name,
                    category = coupon.category.name,
                    expirationTime = coupon.expirationTime,
                    discountType = coupon.discountType.name,
                    discountValue = coupon.discountValue,
                )
        }
    }

    data class ReadCoupon(
        val id: Long,
        val name: String,
        val category: String,
        val expirationTime: LocalDateTime,
        val discountType: String,
        val discountValue: Long,
    ) : CouponResponse() {
        companion object {
            fun from(coupon: Coupon): ReadCoupon =
                ReadCoupon(
                    id = coupon.id,
                    name = coupon.name,
                    category = coupon.category.name,
                    expirationTime = coupon.expirationTime,
                    discountType = coupon.discountType.name,
                    discountValue = coupon.discountValue,
                )
        }
    }

    data class UpdateCoupon(
        val id: Long,
        val name: String,
        val category: String,
        val expirationTime: LocalDateTime,
        val discountType: String,
        val discountValue: Long,
    ) : CouponResponse() {
        companion object {
            fun from(coupon: Coupon): UpdateCoupon =
                UpdateCoupon(
                    id = coupon.id,
                    name = coupon.name,
                    category = coupon.category.name,
                    expirationTime = coupon.expirationTime,
                    discountType = coupon.discountType.name,
                    discountValue = coupon.discountValue,
                )
        }
    }

    data class DeleteCoupon(
        val id: Long,
    ) : CouponResponse()

    data class CreateCouponIssue(
        val id: Long,
        val userId: Long,
        val couponId: Long,
    ) : CouponResponse() {
        companion object {
            fun from(couponIssue: CouponIssue): CreateCouponIssue =
                CreateCouponIssue(
                    id = couponIssue.id,
                    userId = couponIssue.user.id,
                    couponId = couponIssue.coupon.id,
                )
        }
    }

    data class ReadCouponIssue(
        val id: Long,
        val userId: Long,
        val couponId: Long,
    ) : CouponResponse() {
        companion object {
            fun from(couponIssue: CouponIssue): ReadCouponIssue =
                ReadCouponIssue(
                    id = couponIssue.id,
                    userId = couponIssue.user.id,
                    couponId = couponIssue.coupon.id,
                )
        }
    }

    data class UpdateCouponIssue(
        val id: Long,
        val userId: Long,
        val couponId: Long,
    ) : CouponResponse() {
        companion object {
            fun from(couponIssue: CouponIssue): UpdateCouponIssue =
                UpdateCouponIssue(
                    id = couponIssue.id,
                    userId = couponIssue.user.id,
                    couponId = couponIssue.coupon.id,
                )
        }
    }

    data class DeleteCouponIssue(
        val id: Long,
    ) : CouponResponse()
}
