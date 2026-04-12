package small.ecommerce.domain.order.dto

import small.ecommerce.domain.order.OrderStatus
import small.ecommerce.domain.product.ProductSize
import java.math.BigDecimal


sealed class  OrderRequest{

    data class CreateOrder(
        val orderInfo: OrderInfo,
        val orderItems: List<OrderItem>,
    )

    data class OrderItem(
        val productId: Long,
        val productName: String,
        val productPrice: BigDecimal,
        val productSize: ProductSize,
        val productQuantity: Int,
    )

    data class OrderInfo(
        val orderId: Long,
        val customerId: Long,
        val customerAdd: String,
        val orderStatus: OrderStatus,
    )
}
