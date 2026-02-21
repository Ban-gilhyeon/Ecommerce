package small.ecommerce.domain.payment

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.auth.dto.UserPrincipal
import small.ecommerce.domain.exception.PaymentException
import small.ecommerce.domain.order.OrderRepository
import small.ecommerce.domain.order.OrderStatus
import small.ecommerce.domain.payment.dto.PaymentApproveRequest
import small.ecommerce.domain.payment.dto.PaymentApproveResponse

@Service
class PaymentService(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val paymentGatewayManager: PaymentGatewayManager,
) {
    @Transactional
    fun approve(userPrincipal: UserPrincipal, request: PaymentApproveRequest): PaymentApproveResponse {
        val order = orderRepository.findByIdAndUserId(request.orderId, userPrincipal.userId)
            .orElseThrow {
                PaymentException(
                    errorCode = ErrorCode.PAYMENT_ORDER_NOT_FOUND,
                    detail = mapOf("orderId" to request.orderId, "userId" to userPrincipal.userId)
                )
            }

        if (order.status != OrderStatus.PENDING_PAYMENT) {
            throw PaymentException(
                errorCode = ErrorCode.PAYMENT_ALREADY_PROCESSED,
                detail = mapOf("orderId" to order.id, "status" to order.status.name)
            )
        }

        if (paymentRepository.existsByOrderId(order.id)) {
            throw PaymentException(
                errorCode = ErrorCode.PAYMENT_DUPLICATE,
                detail = mapOf("orderId" to order.id)
            )
        }

        val amount: Int = order.orderItem.sumOf { orderItem ->
            orderItem.productPrice * orderItem.quantity
        }
        if (amount <= 0) {
            throw PaymentException(
                errorCode = ErrorCode.PAYMENT_INVALID_AMOUNT,
                detail = mapOf("orderId" to order.id, "amount" to amount)
            )
        }

        val normalizedProvider = paymentGatewayManager.normalizeProvider(request.pgProvider)
        val pgResult = paymentGatewayManager.approve(
            providerCode = normalizedProvider,
            command = PgApproveCommand(
                orderId = order.id,
                amount = amount,
                forceFail = request.forceFail,
                failReason = request.failReason,
            )
        )

        if (pgResult.approved) {
            order.markPaymentApproved()
        } else {
            order.markPaymentFailed()
        }

        val payment = paymentRepository.save(
            Payment(
                orderId = order.id,
                userId = userPrincipal.userId,
                amount = amount,
                transactionId = pgResult.transactionId,
                status = if (pgResult.approved) PaymentStatus.APPROVED else PaymentStatus.DECLINED,
                pgProvider = normalizedProvider,
                failureReason = pgResult.failureReason
            )
        )

        return PaymentApproveResponse(
            paymentId = payment.id,
            orderId = payment.orderId,
            pgProvider = payment.pgProvider,
            transactionId = payment.transactionId,
            paymentStatus = payment.status,
            orderStatus = order.status,
            amount = payment.amount,
            failureReason = payment.failureReason
        )
    }
}
