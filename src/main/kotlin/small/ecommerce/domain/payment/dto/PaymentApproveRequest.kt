package small.ecommerce.domain.payment.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class PaymentApproveRequest(
    @field:NotNull
    @field:Min(1)
    val orderId: Long,
    val pgProvider: String = "MOCK_PG",
    val forceFail: Boolean = false,
    val failReason: String? = null,
)
