package small.ecommerce.api

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import small.ecommerce.domain.auth.dto.UserPrincipal
import small.ecommerce.domain.payment.PaymentService
import small.ecommerce.domain.payment.dto.PaymentApproveRequest
import small.ecommerce.domain.payment.dto.PaymentApproveResponse

@RestController
@RequestMapping("/api/v1/payment")
class PaymentController(
    private val paymentService: PaymentService,
) {
    @PostMapping("/approve")
    fun approve(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @Valid @RequestBody request: PaymentApproveRequest,
    ): ResponseEntity<PaymentApproveResponse> {
        return ResponseEntity.ok(paymentService.approve(userPrincipal, request))
    }
}
