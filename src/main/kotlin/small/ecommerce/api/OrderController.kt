package small.ecommerce.api

import jakarta.validation.Valid
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import small.ecommerce.auth.CustomUserDetails
import small.ecommerce.domain.order.OrderService
import small.ecommerce.domain.order.dto.OrderRequest
import small.ecommerce.domain.order.dto.OrderResponse

@RestController
@RequestMapping("/api/v1/order")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun addOrder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @Valid @RequestBody request: OrderRequest
    ): ResponseEntity<OrderResponse>{
        return ResponseEntity.ok(orderService.createOrder(userDetails.getUser(), request))
    }
}