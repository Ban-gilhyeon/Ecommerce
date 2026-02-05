package small.ecommerce.domain.order

import org.springframework.dao.CannotAcquireLockException
import org.springframework.dao.PessimisticLockingFailureException
import org.springframework.stereotype.Service
import small.ecommerce.domain.auth.dto.UserPrincipal
import small.ecommerce.domain.order.dto.OrderRequest
import small.ecommerce.domain.order.dto.OrderResponse

@Service
class OrderService(
    private val orderCommander: OrderCommander,
) {
    fun createOrder(userPrincipal: UserPrincipal, request: OrderRequest): OrderResponse{
        val productIds: List<Long> = request.itemInfoList.map { it.productId }

        // request에서 주문 수량 합산만 선처리
        val quantityByProductId = mutableMapOf<Long, Int>()
        for(itemInfo in request.itemInfoList){
            val productId = itemInfo.productId
            val quantity = itemInfo.quantity
            quantityByProductId[productId] = ( quantityByProductId[productId] ?: 0 ) + quantity
        }

        return retryOnLock {
            orderCommander.createOrderAndCalculateProductStock(
                userId = userPrincipal.userId,
                productIds = productIds,
                quantityByProductId = quantityByProductId,
            )
        }
    }

    private fun <T> retryOnLock(
        maxAttempts: Int = 3,
        initialBackoffMs: Long = 50,
        block: () -> T,
    ): T {
        var attempt = 1
        var backoff = initialBackoffMs
        while (true) {
            try {
                return block()
            } catch (e: CannotAcquireLockException) {
                if (attempt >= maxAttempts) throw e
            } catch (e: PessimisticLockingFailureException) {
                if (attempt >= maxAttempts) throw e
            }
            try {
                Thread.sleep(backoff)
            } catch (ie: InterruptedException) {
                Thread.currentThread().interrupt()
                throw RuntimeException("Interrupted during lock-retry backoff", ie)
            }
            attempt += 1
            backoff *= 2
        }
    }
}
