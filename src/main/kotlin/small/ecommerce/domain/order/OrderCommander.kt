package small.ecommerce.domain.order

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import small.ecommerce.common.exception.ErrorCode
import small.ecommerce.domain.exception.OrderException
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
        val products = productService.readProductListByProductIdList(productIds)
        val userReference = userService.userReference(userId)
        val nowOrder = Order(user = userReference)

        // product의 stock - quantity 진행
        for (product in products) {
            val productId = product.id
                ?: throw OrderException(
                    errorCode = ErrorCode.ORDER_WRONG_PRODUCT_ID,
                    detail = mapOf("id" to product.id)
                )

            val quantity = quantityByProductId[productId]
                ?: throw OrderException(
                    errorCode = ErrorCode.ORDER_FAIL_OUT_OF_STOCK,
                    detail = mapOf("quantity" to quantityByProductId[productId])
                )

            nowOrder.orderItem.add(
                OrderItem(
                    order = nowOrder,
                    product = product,
                    quantity = quantity,
                    couponIssueId = null,
                )
            )
            // TODO 결제 로직이 있으면 수정하기전에 해야되나??
            productService.soldProduct(product, quantity)
        }
        orderRepo.save(nowOrder)
        return OrderResponse.of(nowOrder)
    }
}
