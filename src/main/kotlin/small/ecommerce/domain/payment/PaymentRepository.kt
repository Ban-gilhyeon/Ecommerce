package small.ecommerce.domain.payment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository : JpaRepository<Payment, Long> {
    fun existsByOrderId(orderId: Long): Boolean
}
