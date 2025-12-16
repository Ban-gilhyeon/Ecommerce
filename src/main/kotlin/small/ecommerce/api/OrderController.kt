package small.ecommerce.api

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import small.ecommerce.domain.order.OrderService

@RestController
@RequestMapping("/api/v1/order")
class OrderController(
    private val OrderService: OrderService
) {
    // 단건 주문

    //여러건 주문
}