package small.ecommerce.domain.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<Product, Long> {
    fun readProductsByBrandId(brandId: Long): MutableList<Product>
}