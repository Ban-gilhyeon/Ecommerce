package small.ecommerce.common.response

data class ApiResponse<T>(
    val status: ResponseStatus,
    val payload: T? = null,
    val error: ErrorPayload? = null,
) {
    companion object {
        fun <T> success(payload: T): ApiResponse<T> =
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                payload = payload,
            )

        fun fail(error: ErrorPayload): ApiResponse<Nothing> =
            ApiResponse(
                status = ResponseStatus.FAIL,
                error = error,
            )
    }
}

data class ErrorPayload(
    val code: String,
    val message: String,
    val detail: Map<String, Any?>? = null,
)

enum class ResponseStatus {
    SUCCESS,
    FAIL
}
