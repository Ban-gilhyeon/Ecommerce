# 결정 사항

- 작성
- 테스트 프로파일은 `application-test.yml`로 분리 작성함
- 테스트 DB는 `ecommerce_test` 고정 사용함
- 300 동시 주문 테스트는 MockMvc + 커스텀 Authentication 주입 방식으로 작성함
- 서버 수정은 Hikari 튜닝 없이 상품별(per-product) 세마포어 벌크헤드로 적용함
- 멀티 상품 주문 데드락 회피를 위해 productId 정렬 후 세마포어 획득/역순 반납 적용함
- 트랜잭션 단축을 위해 `OrderCommander`에서 Product 리스트 사전 조회 제거하고 참조 기반(OrderItem) + 원자 재고 차감 호출로 변경함
