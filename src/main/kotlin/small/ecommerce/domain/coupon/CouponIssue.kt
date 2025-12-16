package small.ecommerce.domain.coupon

import jakarta.persistence.*
import small.ecommerce.domain.BaseTimeEntity
import small.ecommerce.domain.user.User

@Entity
@Table(name = "ConponIssues")
class CouponIssue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", unique = true)
    val coupon: Coupon

): BaseTimeEntity() {
}