package small.ecommerce.domain.brand

import org.springframework.stereotype.Service
import small.ecommerce.auth.UserDetailsService
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.brand.dto.BrandRequest
import small.ecommerce.domain.exception.BrandException
import small.ecommerce.domain.exception.UserException
import small.ecommerce.domain.user.UserRepository

@Service
class BrandService(
    private val brandRepository: BrandRepository,
    private val userRepository: UserRepository,
    private val userDetailsService: UserDetailsService
) {
    //Create
    fun addBrand(request: BrandRequest.Create): Brand {
        val owner = userRepository.findUserById(request.owner)
            ?: throw UserException(
                errorCode = ErrorCode.NOT_FOUND_USER_BY_ID,
                detail = mapOf("id" to request.owner)
            )
        val newBrand = Brand(
            name = request.name,
            description = request.description,
            owner = owner
        )

        return brandRepository.save(newBrand)
    }

    //READ
    //Read List
    fun readAllBrands(): List<Brand>{
        return brandRepository.findAll()
    }

    //Search Brand By Id
    fun readBrandById(brandId: Long): Brand {
        return brandRepository.findById(brandId)
            .orElseThrow{
                BrandException(
                errorCode = ErrorCode.BRAND_NOT_FOUND_BRAND,
                detail = mapOf("id" to brandId)
            )}
    }

    fun updateBrand(brandId: Long, request: BrandRequest.Update): Brand {
        val brand = readBrandById(brandId)

        request.name?.let { brand.name = it }
        request.description?.let { brand.description = it }
        request.owner?.let {
            brand.owner = userRepository.findUserById(it)
                ?: throw UserException(
                    errorCode = ErrorCode.NOT_FOUND_USER_BY_ID,
                    detail = mapOf("id" to it)
                )
        }

        return brandRepository.save(brand)
    }

    fun deleteBrand(brandId: Long) {
        val brand = readBrandById(brandId)
        brandRepository.delete(brand)
    }
}
