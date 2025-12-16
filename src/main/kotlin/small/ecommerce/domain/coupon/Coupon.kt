package small.ecommerce.domain.coupon

import jakarta.persistence.*
import small.ecommerce.domain.BaseTimeEntity
import small.ecommerce.domain.enums.Category
import java.time.LocalDateTime

@Entity
@Table(name = "coupons")
class Coupon(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = true)
    val category: Category,

    @Column(nullable = true)
    val expirationTime: LocalDateTime, // 쿠폰 유효 기간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val discountType:DiscountType,

    @Column(nullable = false)
    val discountValue: Long

): BaseTimeEntity() {
}