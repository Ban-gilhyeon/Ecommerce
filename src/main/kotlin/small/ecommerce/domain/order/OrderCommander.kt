package small.ecommerce.domain.order

import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
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
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun createOrderAndCalculateProductStock(
        userId: Long,
        productIds: List<Long>,
        quantityByProductId: Map<Long, Int>,
    ): OrderResponse {
        log.info(
            "order command start userId={}, requestedProducts={}, mergedProducts={}",
            userId,
            productIds.size,
            quantityByProductId.size,
        )
        val user = userService.getUserByUserId(userId)
        val nowOrder = Order(user = user)

        val uniqueProductIds = productIds.toSet().sorted()
        val productsById = productService.readProductListByProductIdList(uniqueProductIds)
            .associateBy { it.id }

        for (productId in uniqueProductIds) {
            val quantity = quantityByProductId[productId] ?: continue
            val product = productsById[productId]
                ?: continue

            nowOrder.orderItem.add(
                OrderItem(
                    order = nowOrder,
                    productId = product.id,
                    productName = product.name,
                    productPrice = product.price,
                    productCategory = product.category,
                    productGender = product.gender,
                    productSize = product.size,
                    quantity = quantity,
                    couponIssueId = null,
                )
            )
            productService.soldProduct(productId, quantity)
        }

        orderRepo.save(nowOrder)
        log.info(
            "order command success userId={}, orderId={}, itemCount={}",
            userId,
            nowOrder.id,
            nowOrder.orderItem.size,
        )
        return OrderResponse.of(nowOrder)
    }
}
