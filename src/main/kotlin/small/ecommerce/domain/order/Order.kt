package small.ecommerce.domain.order

import jakarta.persistence.*
import org.apache.catalina.mbeans.UserMBean
import small.ecommerce.domain.BaseTimeEntity
import small.ecommerce.domain.product.Product
import small.ecommerce.domain.user.User

@Entity
@Table(name = "orders")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(nullable = false)
    val user: User,

    @ManyToMany
    @JoinColumn(nullable = false)
    val products: List<Product>,

    @Column(nullable = false)
    val status: OrderStatus,

): BaseTimeEntity(){
}