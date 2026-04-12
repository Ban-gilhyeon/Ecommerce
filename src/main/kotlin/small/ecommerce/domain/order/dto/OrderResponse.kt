package small.ecommerce.domain.order.dto

import small.ecommerce.domain.order.OrderItem
import small.ecommerce.domain.order.OrderStatus
import small.ecommerce.domain.product.ProductSize
import java.math.BigDecimal

sealed class OrderResponse {
    data class CreateOrder(
        val orderInfo: OrderInfo,
        val totalAmount: BigDecimal,
        val orderItems: List<OrderItem>,
    )
    data class OrderInfo(
        val orderId: Long,
        val customerId: Long,
        val customerAdd: String,

        //todo : 결제 로직 추가 시 nullable = false
        val paymentInfo: PaymentInfo? = null,
    )

    data class OrderItem(
        val brandId: Long,
        val brandName: String,
        val productId: Long,
        val productName: String,
        val productPrice: BigDecimal,
        val productSize: ProductSize,
        val productQuantity: Int,
        val orderStatus: OrderStatus,
    )

    //구현 전
    data class PaymentInfo(
        val cardNumber: String? = null,
        val cardCompany: String? = null,
    )
}