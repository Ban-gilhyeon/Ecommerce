package small.ecommerce.domain.product.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ProductAddRequest(
    @field:NotBlank(message = "상품 이름을 입력해주세요")
    val name: String,

    @field:NotBlank(message = "상품 사이즈를 입력해주세요")
    val size: String,

    @field:NotBlank(message = "상품 카테고리를 입력해주세요")
    val category: String,

    @field:NotBlank(message = "상품 성별을 입력해주세요")
    val gender: String,

    @field:NotNull(message = "상품 재고 개수 입력해주세요")
    val stock: Int,

    @field:NotNull(message = "상품 브랜드를 입력해주세요")
    val brandId: Long,

    @field:NotNull(message = "상품 가격을 입력해주세요")
    val price: Int
) {
}