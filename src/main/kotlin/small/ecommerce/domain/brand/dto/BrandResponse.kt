package small.ecommerce.domain.brand.dto

import small.ecommerce.domain.brand.Brand

sealed class BrandResponse {
    data class Create(
        val id: Long,
        val name: String,
        val description: String?,
        val owner: Long,
    ) : BrandResponse() {
        companion object {
            fun from(brand: Brand): Create =
                Create(
                    id = brand.id,
                    name = brand.name,
                    description = brand.description,
                    owner = brand.owner.id,
                )
        }
    }

    data class Read(
        val id: Long,
        val name: String,
        val description: String?,
        val owner: Long,
        val ownerName: String,
    ) : BrandResponse() {
        companion object {
            fun from(brand: Brand): Read =
                Read(
                    id = brand.id,
                    name = brand.name,
                    description = brand.description,
                    owner = brand.owner.id,
                    ownerName = brand.owner.name,
                )
        }
    }

    data class Update(
        val id: Long,
        val name: String,
        val description: String?,
        val owner: Long,
        val ownerName: String,
    ) : BrandResponse() {
        companion object {
            fun from(brand: Brand): Update =
                Update(
                    id = brand.id,
                    name = brand.name,
                    description = brand.description,
                    owner = brand.owner.id,
                    ownerName = brand.owner.name,
                )
        }
    }

    data class Delete(
        val id: Long,
    ) : BrandResponse()
}
