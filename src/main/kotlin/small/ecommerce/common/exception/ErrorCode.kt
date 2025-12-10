package small.ecommerce.common.exception

enum class ErrorCode(
    val code: String,
    val message: String,
    val httpStatus: Int
) {
    // 4xx Client Errors
    INVALID_REQUEST("C001", "잘못된 요청입니다.", 400),
    INVALID_PARAMETER("C002", "유효하지 않은 파라미터입니다.", 400),
    MISSING_PARAMETER("C003", "필수 파라미터가 누락되었습니다.", 400),
    INVALID_JSON("C004", "잘못된 JSON 형식입니다.", 400),
    USER_ID_REQUIRED("C005", "사용자 ID가 필요합니다.", 400),
    INVALID_END_TIME("C006", "모임 종료 시간이 현재 시간보다 이전일 수 없습니다. 종료 시간을 다시 확인해주세요.", 400),
    MEETING_ALREADY_CLOSED("C4097", "이미 종료된 모임입니다.", 400),

    // JWT 토큰 관련 에러 (J001~J099)
    JWT_TOKEN_MISSING("J001", "JWT 토큰이 누락되었습니다.", 401),
    JWT_TOKEN_INVALID("J002", "유효하지 않은 JWT 토큰입니다.", 401),
    JWT_TOKEN_EXPIRED("J003", "만료된 JWT 토큰입니다.", 401),
    JWT_TOKEN_MALFORMED("J004", "잘못된 형식의 JWT 토큰입니다.", 401),
    JWT_SIGNATURE_INVALID("J005", "JWT 서명이 유효하지 않습니다.", 401),
    JWT_TOKEN_UNSUPPORTED("J006", "지원하지 않는 JWT 토큰입니다.", 401),
    ACCESS_TOKEN_INVALID("J007", "유효하지 않은 Access Token입니다.", 401),
    REFRESH_TOKEN_INVALID("J008", "유효하지 않은 Refresh Token입니다.", 401),
    REFRESH_TOKEN_EXPIRED("J009", "만료된 Refresh Token입니다.", 401),
    REFRESH_TOKEN_MISMATCH("J010", "Refresh Token이 일치하지 않습니다.", 401),
    USER_NOT_FOUND_FOR_TOKEN("J011", "토큰에 해당하는 사용자를 찾을 수 없습니다.", 404),
    TOKEN_USER_ID_INVALID("J012", "토큰의 사용자 ID가 유효하지 않습니다.", 401),

    //user
    NOT_FOUND_USER_BY_ID("U000", "회원을 찾을 수 없습니다.", 404),
    NOT_FOUND_USER_BY_EMAIL("U001", "해당 이메일의 사용자를 찾을 수 없습니다..", 404),
    USER_ALREADY_EXISTS("U002", "이미 가입된 이메일입니다.", 400),

    //Brand
    BRAND_NOT_FOUND_BRAND("B000", "해당 브랜드를 찾을 수 없습니다.", 404),
    BRAND_ALREADY_EXISTS("B001", "이미 등록된 브랜드입니다.", 400),

    //Product
    PRODUCT_NOT_FOUND_PRODUCT_BY_ID("P000", "헤당 상품을 찾을 수 없습니다.",404),

}