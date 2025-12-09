package small.ecommerce.domain.Brand.dto

import small.ecommerce.domain.Brand.Brand

data class BrandInfoResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val ownerName: String
) {
    companion object{
        fun from(brand: Brand): BrandInfoResponse =
            BrandInfoResponse(
                id = brand.id,
                name = brand.name,
                description = brand.description,
                ownerName = brand.owner.name
            )
    }
}