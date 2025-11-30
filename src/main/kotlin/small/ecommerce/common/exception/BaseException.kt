package small.ecommerce.common.exception

abstract class BaseException(
    val errorCode: ErrorCode,
    val detail: Map<String, Any?>? = null,
    message: String? = errorCode.message,
): RuntimeException(message){

}