package small.ecommerce.domain.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductStockRepository : JpaRepository<ProductStock, Long> {
    fun findAllByProductId(productId: Long): List<ProductStock>
}
