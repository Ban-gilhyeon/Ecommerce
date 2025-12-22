package small.ecommerce.domain.order

import jakarta.persistence.*
import org.apache.catalina.mbeans.UserMBean
import small.ecommerce.domain.BaseTimeEntity
import small.ecommerce.domain.coupon.CouponIssue
import small.ecommerce.domain.product.Product
import small.ecommerce.domain.user.User
import javax.annotation.OverridingMethodsMustInvokeSuper

@Entity
@Table(name = "orders")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(nullable = false)
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

    // 결제가 추가되면 필요한 응답 넣어주기
    //결제를 완료한 경우
    fun pay(order:Order) {
        order.status = OrderStatus.PROCESSING
    }
}