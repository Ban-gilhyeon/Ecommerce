package small.ecommerce.domain.brand.dto

sealed class BrandRequest{

    data class Create(
        val name: String,
        val description: String,
        val owner: Long
    ) : BrandRequest()

    data class Update(
        val name: String? = null,
        val description: String? = null,
        val owner: Long? = null
    ) : BrandRequest()
}
