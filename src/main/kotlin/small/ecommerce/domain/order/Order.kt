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
            val order = Order(
                user = user,
                orderItem = orderItems,
            )
            orderItems.forEach { it.order = order }
            return order
        }
    }

    // 결제가 추가되면 필요한 응답 넣어주기
    //결제를 완료한 경우
    fun pay(order:Order) {
        order.status = OrderStatus.PROCESSING
    }
}
