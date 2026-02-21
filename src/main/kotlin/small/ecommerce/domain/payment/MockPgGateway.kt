package small.ecommerce.domain.payment

import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class MockPgGateway : PgGateway {
    private val providerName = "MOCK_PG"

    override fun providerCode(): String = providerName

    override fun approve(command: PgApproveCommand): PgApproveResult {
        val txId = "MOCK-TX-${command.orderId}-${LocalDateTime.now().nano}"
        if (command.forceFail) {
            return PgApproveResult(
                approved = false,
                transactionId = txId,
                failureReason = command.failReason ?: "Mock PG에서 결제가 거절되었습니다."
            )
        }

        return PgApproveResult(
            approved = true,
            transactionId = txId,
            failureReason = null
        )
    }
}
