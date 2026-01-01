package small.ecommerce.domain.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<Product, Long> {
    fun readProductsByBrandId(brandId: Long): MutableList<Product>

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update  Product p
        set p.stock = p.stock - :quantity
        where p.id = :productId and p.stock >= :quantity
    """
    )
    fun decreaseStock(productId: Long, quantity: Int): Int
    //fun findAllById(productIds: List<Long>): List<Product>

}