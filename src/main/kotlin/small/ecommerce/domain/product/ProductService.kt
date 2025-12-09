package small.ecommerce.domain.product

import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepo: ProductRepository
) {

}