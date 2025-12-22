package small.ecommerce.domain.order.dto

import small.ecommerce.domain.order.Order
import small.ecommerce.domain.order.OrderItem
import small.ecommerce.domain.order.OrderStatus
import small.ecommerce.domain.product.Product
import small.ecommerce.domain.user.User

data class OrderResponse(
    val orderId: Long,
    val userId: Long,
    val userAddress: String,
    val orderItems: List<OrderItem>,
    val status: OrderStatus

) {
    companion object{
        fun of(order: Order): OrderResponse{
            val response = OrderResponse(
                orderId = order.id,
                userId =order.user.id,
                userAddress = order.user.address,
                orderItems = order.orderItem,
                status = order.status
            )
            return response
        }
    }
}