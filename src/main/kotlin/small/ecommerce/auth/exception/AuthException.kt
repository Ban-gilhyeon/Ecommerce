package small.ecommerce.auth.exception

import small.ecommerce.common.exception.BaseException
import small.ecommerce.common.exception.ErrorCode

class AuthException(
    errorCode: ErrorCode,
    detail: Map<String, Any?>? = null,
    message: String? = null
):BaseException(
    errorCode = errorCode,
    detail = detail,
    message = message ?: errorCode.message
) {
}