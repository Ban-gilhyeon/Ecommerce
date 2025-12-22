package small.ecommerce.domain.order.dto

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item

data class OrderRequest(
    val itemInfoList: List<ItemInfo>,
) {
}

data class ItemInfo(
    val productId: Long,
    val quantity: Int,
    val couponIssueId: Long?,
){
}
