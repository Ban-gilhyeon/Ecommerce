package small.ecommerce.domain.Brand.dto

import small.ecommerce.domain.Brand.Brand

data class BrandAddResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val owner: Long
) {
    companion object{
        fun of(brand: Brand): BrandAddResponse =
            BrandAddResponse(
                id = brand.id,
                name = brand.name,
                description = brand.description,
                owner = brand.owner.id
            )

    }
}