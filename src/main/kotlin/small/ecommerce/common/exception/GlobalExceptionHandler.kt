package small.ecommerce.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import small.ecommerce.common.response.ApiResponse
import small.ecommerce.common.response.ErrorPayload

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BaseException::class)
    fun handleBaseException(exception: BaseException): ResponseEntity<ApiResponse<Nothing>> {
        val errorCode = exception.errorCode
        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(
                ApiResponse.fail(
                    ErrorPayload(
                        code = errorCode.code,
                        message = exception.message ?: errorCode.message,
                        detail = exception.detail,
                    )
                )
            )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ApiResponse.fail(
                    ErrorPayload(
                        code = ErrorCode.INVALID_PARAMETER.code,
                        message = exception.message ?: ErrorCode.INVALID_PARAMETER.message,
                    )
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiResponse.fail(
                    ErrorPayload(
                        code = "S000",
                        message = exception.message ?: "서버 오류가 발생했습니다.",
                    )
                )
            )
    }
}
