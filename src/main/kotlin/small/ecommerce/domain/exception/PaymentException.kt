package small.ecommerce.domain.exception

import small.ecommerce.common.exception.BaseException
import small.ecommerce.common.exception.ErrorCode

class PaymentException(
    errorCode: ErrorCode,
    detail: Map<String, Any?>? = null,
    message: String? = errorCode.message,
): BaseException(
    errorCode = errorCode,
    detail = detail,
    message = message
)
