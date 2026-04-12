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
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    var category: Category,

    @Column(nullable = true)
    var expirationTime: LocalDateTime, // 쿠폰 유효 기간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var discountType:DiscountType,

    @Column(nullable = false)
    var discountValue: Long

): BaseTimeEntity() {
}
