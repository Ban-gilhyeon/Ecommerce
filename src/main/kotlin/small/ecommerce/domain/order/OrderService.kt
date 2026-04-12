package small.ecommerce.domain.order

import org.springframework.stereotype.Service
import small.ecommerce.domain.product.ProductService
import small.ecommerce.domain.user.UserService

@Service
class OrderService(
    private val orderRepo: OrderRepository,
    private val userService: UserService,
    private val productService: ProductService,
) {

}
