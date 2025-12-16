package small.ecommerce.domain.order

enum class OrderStatus{
    PENDING_PAYMENT, // 결제 대기중
    FAILED, // 주문 실패
    PROCESSING, // 처리중
    HOLD, // 보류중
    COMPLETED, //완료
    CANCELED, // 취소
    REFUNDED, //환불
}