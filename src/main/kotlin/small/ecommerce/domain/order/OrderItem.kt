package small.ecommerce.domain.order

import jakarta.persistence.*
import small.ecommerce.domain.coupon.CouponIssue
import small.ecommerce.domain.product.Product

@Entity
class OrderItem(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product,

    val quantity: Int,

    val couponIssueId: Long? = null
) {
    /*companion object{
        fun from(product: Product, quantity: Int): OrderItem{
            return OrderItem(

            )
        }
    }*/
}