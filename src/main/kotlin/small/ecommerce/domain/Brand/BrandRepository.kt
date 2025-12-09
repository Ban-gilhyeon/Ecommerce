package small.ecommerce.domain.Brand

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandRepository: JpaRepository<Brand, Long> {
    fun findAllByName(name: String): MutableList<Brand>
}