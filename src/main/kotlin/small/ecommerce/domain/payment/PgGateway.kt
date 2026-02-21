package small.ecommerce.domain.payment

interface PgGateway {
    fun providerCode(): String
    fun approve(command: PgApproveCommand): PgApproveResult
}

data class PgApproveCommand(
    val orderId: Long,
    val amount: Int,
    val forceFail: Boolean,
    val failReason: String?,
)

data class PgApproveResult(
    val approved: Boolean,
    val transactionId: String,
    val failureReason: String?,
)
