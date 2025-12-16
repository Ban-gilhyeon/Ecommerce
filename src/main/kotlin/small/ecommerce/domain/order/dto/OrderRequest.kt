package small.ecommerce.domain.order.dto

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item

data class OrderRequest(
    val userId: Long,
) {
}