package small.ecommerce.domain.order

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import small.ecommerce.domain.order.dto.OrderResponse
import small.ecommerce.domain.product.ProductService
import small.ecommerce.domain.user.UserService

@Service
class OrderCommander(
    private val orderRepo: OrderRepository,
    private val userService: UserService,
    private val productService: ProductService,
) {
    @Transactional
    fun createOrderAndCalculateProductStock(
        userId: Long,
        productIds: List<Long>,
        quantityByProductId: Map<Long, Int>,
    ): OrderResponse {
        val userReference = userService.userReference(userId)
        val nowOrder = Order(user = userReference)

        val uniqueProductIds = productIds.toSet().sorted()
        for (productId in uniqueProductIds) {
            val quantity = quantityByProductId[productId] ?: continue
            val productReference = productService.productReference(productId)

            nowOrder.orderItem.add(
                OrderItem(
                    order = nowOrder,
                    product = productReference,
                    quantity = quantity,
                    couponIssueId = null,
                )
            )
            productService.soldProduct(productId, quantity)
        }

        orderRepo.save(nowOrder)
        return OrderResponse.of(nowOrder)
    }
}
