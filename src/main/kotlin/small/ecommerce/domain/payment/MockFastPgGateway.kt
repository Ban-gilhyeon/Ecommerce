package small.ecommerce.domain.payment

import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class MockFastPgGateway : PgGateway {
    override fun providerCode(): String = "MOCK_FAST_PG"

    override fun approve(command: PgApproveCommand): PgApproveResult {
        val txId = "FAST-TX-${command.orderId}-${LocalDateTime.now().nano}"
        if (command.forceFail) {
            return PgApproveResult(
                approved = false,
                transactionId = txId,
                failureReason = command.failReason ?: "Mock Fast PG에서 결제가 거절되었습니다."
            )
        }

        return PgApproveResult(
            approved = true,
            transactionId = txId,
            failureReason = null
        )
    }
}
