package small.ecommerce.domain.product

import jakarta.persistence.Table
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import small.ecommerce.domain.BaseTimeEntity

@Entity
@Table(name = "product_stocks")
class ProductStock(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var productId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var size: ProductSize,

    @Column(nullable = false)
    var color: String,

    @Column(nullable = false)
    var quantity: Int,

): BaseTimeEntity(){
    
}
