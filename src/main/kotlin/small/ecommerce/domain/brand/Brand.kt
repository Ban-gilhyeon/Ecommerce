package small.ecommerce.domain.brand

import jakarta.persistence.*
import small.ecommerce.domain.BaseTimeEntity
import small.ecommerce.domain.user.User

@Entity
@Table(name = "brands")
class Brand(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = true)
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    var owner: User

): BaseTimeEntity() {
}
