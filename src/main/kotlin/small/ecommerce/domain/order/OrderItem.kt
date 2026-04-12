package small.ecommerce.domain.order

import jakarta.persistence.*

@Entity
class OrderItem(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order? = null,

    @Column(nullable = false)
    val productId: Long,

   @Column(nullable = false)
    val stockId: Long,
) {
}
