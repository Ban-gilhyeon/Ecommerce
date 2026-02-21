package small.ecommerce.domain.payment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import small.ecommerce.domain.BaseTimeEntity

@Entity
@Table(
    name = "payments",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_payments_order_id", columnNames = ["order_id"]),
        UniqueConstraint(name = "uk_payments_transaction_id", columnNames = ["transaction_id"]),
    ]
)
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "order_id", nullable = false)
    val orderId: Long,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val amount: Int,

    @Column(name = "transaction_id", nullable = false, length = 120)
    val transactionId: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val status: PaymentStatus,

    @Column(name = "pg_provider", nullable = false, length = 40)
    val pgProvider: String,

    @Column(name = "failure_reason", length = 200)
    val failureReason: String? = null,
) : BaseTimeEntity()
