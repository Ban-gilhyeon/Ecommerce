package small.ecommerce.domain.Brand

import org.springframework.stereotype.Service
import small.ecommerce.auth.UserDetailsService
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.Brand.dto.BrandAddRequest
import small.ecommerce.domain.Brand.dto.BrandInfoResponse
import small.ecommerce.domain.exception.BrandException
import small.ecommerce.domain.exception.UserException
import small.ecommerce.domain.user.UserRepository
import java.util.*

@Service
class BrandService(
    private val brandRepository: BrandRepository,
    private val userRepository: UserRepository,
    private val userDetailsService: UserDetailsService
) {
    //Create
    fun addBrand(request: BrandAddRequest): Brand{
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

        brandRepository.save(newBrand)
        return newBrand
    }

    //READ
    //Read List
    fun readAllBrandInfoList(): List<BrandInfoResponse>{
        val list: List<Brand> = brandRepository.findAll()
        return list.map { BrandInfoResponse.from(it) }
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
}