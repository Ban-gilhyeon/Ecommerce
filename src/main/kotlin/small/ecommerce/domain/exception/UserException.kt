package small.ecommerce.domain.exception

import small.ecommerce.common.exception.BaseException
import small.ecommerce.common.exception.ErrorCode

class UserException(
    errorCode: ErrorCode,
    detail: Map<String, Any?>? = null,
    message: String? = null
): BaseException(
    errorCode = errorCode,
    detail = detail,
    message = message ?: errorCode.message
) {
}