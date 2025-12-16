package small.ecommerce.domain.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository: JpaRepository<Order,Long> {
}