package small.ecommerce.domain.user.dto

import jakarta.validation.constraints.NotBlank

data class UserSignUpRequest(
    @field:NotBlank(message = "이메일을 입력해주세요")
    val email: String,

    @field:NotBlank(message = "비밀번호를 입력해주세요")
    val password: String,

    @field:NotBlank(message = "이름을 입력해주세요")
    val name: String,

    //임시 예외처리
    @field:NotBlank(message = "권한 입력해주세요")
    val role: String
) {
}