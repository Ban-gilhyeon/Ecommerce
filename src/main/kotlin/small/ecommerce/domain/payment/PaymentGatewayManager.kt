package small.ecommerce.domain.payment

import org.springframework.stereotype.Component
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.exception.PaymentException
import java.util.Locale

@Component
class PaymentGatewayManager(
    gateways: List<PgGateway>,
) {
    private val gatewaysByCode: Map<String, PgGateway> = gateways.associateBy { normalize(it.providerCode()) }

    fun normalizeProvider(providerCode: String): String {
        return normalize(providerCode)
    }

    fun approve(providerCode: String, command: PgApproveCommand): PgApproveResult {
        val normalizedProviderCode = normalize(providerCode)
        val gateway = gatewaysByCode[normalizedProviderCode]
            ?: throw PaymentException(
                errorCode = ErrorCode.PAYMENT_PROVIDER_NOT_SUPPORTED,
                detail = mapOf("pgProvider" to providerCode)
            )
        return gateway.approve(command)
    }

    private fun normalize(providerCode: String): String {
        return providerCode.trim().uppercase(Locale.ROOT)
    }
}
