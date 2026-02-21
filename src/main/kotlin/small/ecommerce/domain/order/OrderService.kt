package small.ecommerce.domain.order

import org.slf4j.LoggerFactory
import org.springframework.dao.CannotAcquireLockException
import org.springframework.dao.PessimisticLockingFailureException
import org.springframework.stereotype.Service
import small.ecommerce.domain.auth.dto.UserPrincipal
import small.ecommerce.domain.order.dto.OrderRequest
import small.ecommerce.domain.order.dto.OrderResponse
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Semaphore

@Service
class OrderService(
    private val orderCommander: OrderCommander,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val productBulkheads = ConcurrentHashMap<Long, Semaphore>()
    private val maxConcurrentOrdersPerProduct = 5

    fun createOrder(userPrincipal: UserPrincipal, request: OrderRequest): OrderResponse{
        val productIds: List<Long> = request.itemInfoList.map { it.productId }
        log.info(
            "order create requested userId={}, itemCount={}, productCount={}",
            userPrincipal.userId,
            request.itemInfoList.size,
            productIds.toSet().size,
        )

        // request에서 주문 수량 합산만 선처리
        val quantityByProductId = mutableMapOf<Long, Int>()
        for(itemInfo in request.itemInfoList){
            val productId = itemInfo.productId
            val quantity = itemInfo.quantity
            quantityByProductId[productId] = ( quantityByProductId[productId] ?: 0 ) + quantity
        }

        val response = withProductBulkhead(productIds) {
            retryOnLock {
                orderCommander.createOrderAndCalculateProductStock(
                    userId = userPrincipal.userId,
                    productIds = productIds,
                    quantityByProductId = quantityByProductId,
                )
            }
        }
        log.info(
            "order create completed userId={}, orderId={}, status={}",
            userPrincipal.userId,
            response.orderId,
            response.status,
        )
        return response
    }

    private fun withProductBulkhead(productIds: List<Long>, block: () -> OrderResponse): OrderResponse {
        val uniqueSortedProductIds = productIds.toSet().sorted()
        val acquiredSemaphores = mutableListOf<Semaphore>()

        try {
            for (productId in uniqueSortedProductIds) {
                val semaphore = productBulkheads.computeIfAbsent(productId) {
                    Semaphore(maxConcurrentOrdersPerProduct, true)
                }
                semaphore.acquire()
                acquiredSemaphores.add(semaphore)
            }
            return block()
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            throw RuntimeException("주문 동시성 제어 중 인터럽트가 발생했습니다.", e)
        } finally {
            acquiredSemaphores.asReversed().forEach { it.release() }
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
