package small.ecommerce.domain.product

import jakarta.persistence.*
import small.ecommerce.domain.BaseTimeEntity
import small.ecommerce.domain.Brand.Brand
import small.ecommerce.domain.enums.Category
import small.ecommerce.domain.enums.Gender

@Entity
@Table(name = "products")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val price: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    val brand: Brand,

    @Column(nullable = false)
    var stock: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val category: Category,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val gender: Gender,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val size: ProductSize,

    ):BaseTimeEntity() {
}