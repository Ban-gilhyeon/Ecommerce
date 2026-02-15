# 학습/인사이트

- 작성
- `src/test/resources/application-test.yml` 작성 시 `spring.profiles.active=test`로 main 설정을 상속하고 datasource/jpa만 덮어쓰기 가능함
- Spring Boot 4 + 현재 의존성 조합에서 `@AutoConfigureMockMvc` import 이슈가 있어 `MockMvcBuilders.webAppContextSetup(...)` 방식으로 테스트 구성함
- 컨트롤러 principal 타입이 `UserPrincipal` 이므로 `SecurityMockMvcRequestPostProcessors.authentication(...)` 으로 커스텀 Authentication 주입 방식 사용함
- 동일 상품 동시 주문 경합 완화를 위해 DB 접근 전 상품별 세마포어 획득 방식 적용함
- 트랜잭션 내부 SELECT 축소를 위해 Product 전체 조회 대신 `getReferenceById` + 조건부 원자 UPDATE 흐름으로 변경함
