package small.ecommerce.domain.payment.dto

import small.ecommerce.domain.order.OrderStatus
import small.ecommerce.domain.payment.PaymentStatus

data class PaymentApproveResponse(
    val paymentId: Long,
    val orderId: Long,
    val pgProvider: String,
    val transactionId: String,
    val paymentStatus: PaymentStatus,
    val orderStatus: OrderStatus,
    val amount: Int,
    val failureReason: String? = null,
)
