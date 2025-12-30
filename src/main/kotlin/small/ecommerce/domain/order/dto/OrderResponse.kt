package small.ecommerce.domain.order.dto

import small.ecommerce.domain.enums.Category
import small.ecommerce.domain.enums.Gender
import small.ecommerce.domain.order.Order
import small.ecommerce.domain.order.OrderItem
import small.ecommerce.domain.order.OrderStatus
import small.ecommerce.domain.product.Product
import small.ecommerce.domain.product.ProductSize
import small.ecommerce.domain.user.User

data class OrderResponse(

    val orderId: Long,
    val userId: Long,
    val userName: String,
    val userAddress: String,
    val orderItemsResponse: List<OrderItemResponse>,
    val status: OrderStatus

) {
    companion object{
        fun of(order: Order): OrderResponse{
            var orderItemResponses =  mutableListOf<OrderItemResponse>()
            for(productInfo in order.orderItem){
                val orderItemResponse = OrderItemResponse(
                    productId = productInfo.product.id,
                    productName = productInfo.product.name,
                    productCategory = productInfo.product.category,
                    productGender = productInfo.product.gender,
                    productPrice = productInfo.product.price,
                    productSize = productInfo.product.size
                )
                orderItemResponses.add(orderItemResponse)
            }
            val response = OrderResponse(
                orderId = order.id,
                userId =order.user.id,
                userName = order.user.name,
                userAddress = order.user.address,
                orderItemsResponse = orderItemResponses,
                status = order.status
            )
            return response
        }
    }

    data class OrderItemResponse(
        val productId: Long,
        val productName: String,
        val productPrice: Int,
        val productCategory: Category,
        val productGender: Gender,
        val productSize: ProductSize,


    ){}
}