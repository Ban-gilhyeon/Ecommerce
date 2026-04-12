package small.ecommerce.domain.product

import jakarta.persistence.*
import small.ecommerce.domain.BaseTimeEntity
import small.ecommerce.domain.enums.Category
import small.ecommerce.domain.enums.Gender

@Entity
@Table(name = "products")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var brandId: Long,

    @Column(nullable = false)
    var stockId: Long,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var price: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var category: Category,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var gender: Gender,

    ):BaseTimeEntity() {

}
