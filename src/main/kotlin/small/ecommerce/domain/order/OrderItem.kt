package small.ecommerce.domain.order

import jakarta.persistence.*
import small.ecommerce.domain.enums.Category
import small.ecommerce.domain.enums.Gender
import small.ecommerce.domain.product.ProductSize

@Entity
class OrderItem(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    @Column(name = "product_id", nullable = false)
    val productId: Long,

    @Column(name = "product_name", nullable = false)
    val productName: String,

    @Column(name = "product_price", nullable = false)
    val productPrice: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", nullable = false)
    val productCategory: Category,

    @Enumerated(EnumType.STRING)
    @Column(name = "product_gender", nullable = false)
    val productGender: Gender,

    @Enumerated(EnumType.STRING)
    @Column(name = "product_size", nullable = false)
    val productSize: ProductSize,

    val quantity: Int,

    val couponIssueId: Long? = null
) {
}
