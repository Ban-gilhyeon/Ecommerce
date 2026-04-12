package small.ecommerce.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Order", description = "Order API")
@SecurityRequirement(name = "bearerAuth")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    @Operation(summary = "Create order")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Order created"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
        ],
    )
    fun addOrder(
        @Parameter(hidden = true)
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @Valid @RequestBody request: OrderRequest.CreateOrder
    ): ResponseEntity<Unit>{

        return ResponseEntity.noContent().build()
    }
}
