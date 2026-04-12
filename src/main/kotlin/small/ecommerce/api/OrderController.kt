package small.ecommerce.api

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import small.ecommerce.auth.CustomUserDetails
import small.ecommerce.domain.order.OrderService
import small.ecommerce.domain.order.dto.OrderRequest


@RestController
@RequestMapping("/api/v1/order")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun addOrder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @Valid @RequestBody request: OrderRequest.CreateOrder
    ): ResponseEntity<Unit>{

        return ResponseEntity.noContent().build()
    }
}
