package small.ecommerce.domain.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductStockRepository : JpaRepository<ProductStock, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        """
        update ProductStock ps
        set ps.availableStock = ps.availableStock - :quantity
        where ps.productId = :productId and ps.availableStock >= :quantity
        """
    )
    fun decreaseStock(productId: Long, quantity: Int): Int

    fun findAllByProductIdIn(productIds: Collection<Long>): List<ProductStock>
}
