package small.ecommerce.domain.product

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "product_stock")
class ProductStock(
    @Id
    @Column(name = "product_id")
    val productId: Long,

    @Column(name = "available_stock", nullable = false)
    var availableStock: Int,
)
