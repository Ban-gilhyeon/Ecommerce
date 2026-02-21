package small.ecommerce.domain.order

import jakarta.persistence.*
import small.ecommerce.domain.BaseTimeEntity
import small.ecommerce.domain.user.User

@Entity
@Table(name = "orders")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    val user: User,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderItem: MutableList<OrderItem> = mutableListOf(),

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: OrderStatus = OrderStatus.PENDING_PAYMENT,

    ): BaseTimeEntity(){
    companion object {
        fun from(user: User, orderItems: MutableList<OrderItem>): Order {
            return Order(
                user = user,
                orderItem = orderItems,
            )
        }
    }

    // 결제 승인 이후 주문 상태를 처리중으로 전이한다.
    fun markPaymentApproved() {
        status = OrderStatus.PROCESSING
    }

    // 결제가 거절되면 주문 상태를 실패로 전이한다.
    fun markPaymentFailed() {
        status = OrderStatus.FAILED
    }
}
