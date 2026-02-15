# 동일 상품 300건 이상 동시 주문 시 DB 타임아웃 해결 (레포 내 부하/동시성 테스트 포함)

## TL;DR

> **요약**: MySQL `ecommerce_test`에 붙는 MockMvc 기반 “300 동시 주문” 재현 테스트를 먼저 추가하고, 단일 인스턴스 전제에서 상품별(per-product) 인메모리 벌크헤드(세마포어)로 DB 진입 동시성을 제한해 DB/Hikari 타임아웃을 제거한다. 동시에 트랜잭션 내부 작업을 줄여 락 홀드 시간/커넥션 점유 시간을 단축한다.
>
> **산출물**:
> - JMeter 없이도 언제든 재현 가능한 레포 내 동시성/부하 테스트
> - 동일 상품 스파이크에서도 Hikari 튜닝 없이(주 해결책으로는) DB/Hikari 타임아웃이 발생하지 않는 서버 로직
> - “타임아웃 없음 + 오버셀 없음(재고 음수 금지)” 회귀 검증
>
> **예상 공수**: Medium
> **병렬 실행**: 가능 (2 waves)
> **크리티컬 패스**: 재현 테스트 추가 → 베이스라인 관측 → 벌크헤드/트랜잭션 개선 → 3회 연속 green

---

## 배경

### 원래 요청
- 개인 이커머스 프로젝트.
- 동일 상품에 대해 300건 이상 동시 주문이 들어오면 DB 타임아웃 발생.
- 히카리풀(HikariCP)은 최대한 건드리지 않고, 로직/트랜잭션 관리로 해결하고 싶음.
- JMeter 대신 레포 안에서 실행 가능한 “코드 기반 재현 테스트”를 원함.

### 결정 사항(확정)
- **부하 테스트 방식**: MockMvc 기반 동시 요청 테스트.
- **테스트 DB**: 로컬 MySQL 스키마 `ecommerce_test`.
- **작업 범위**: 재현 테스트 추가 + 서버 수정까지 포함.
- **배포 형태**: 단일 인스턴스(인메모리 제어 가능).
- **자동화 테스트**: Tests-after.

### 레포 근거(증거)
- 주문 API:
  - `src/main/kotlin/small/ecommerce/api/OrderController.kt` 에 `POST /api/v1/order` 존재.
  - `@AuthenticationPrincipal userPrincipal: UserPrincipal` 을 요구.
  - 요청 DTO: `src/main/kotlin/small/ecommerce/domain/order/dto/OrderRequest.kt`.
- 보안:
  - `src/main/kotlin/small/ecommerce/auth/config/SecurityConfig.kt`: CSRF 비활성화, whitelist 외는 인증 필요.
  - `src/main/kotlin/small/ecommerce/auth/jwt/JwtAuthFilter.kt`: principal을 `UserPrincipal`로 세팅.
  - 따라서 MockMvc 테스트에서 `@WithMockUser` 대신 **커스텀 principal 주입**이 필요.
- 재고 차감의 정합성 핵심:
  - `src/main/kotlin/small/ecommerce/domain/product/ProductRepository.kt`: `stock = stock - qty WHERE id = ? AND stock >= qty` 형태의 조건부 원자 UPDATE.
  - `src/main/kotlin/small/ecommerce/domain/product/Product.kt`: `@Version` 없음(낙관락 없음).
- 트랜잭션 경계:
  - `src/main/kotlin/small/ecommerce/domain/order/OrderCommander.kt`: `jakarta.transaction.Transactional` 사용.
- DB 설정:
  - `src/main/resources/application.yml`: MySQL `ecommerce`에 root/1234; `spring.datasource.hikari.*` 명시 없음.

### Metis 리뷰(가드레일 반영)
- 풀 사이즈 증설 같은 Hikari 튜닝으로 “가리는” 방식이 아니라, **DB 커넥션 점유를 줄이는 방식**을 우선.
- 벌크헤드/스로틀은 **트랜잭션/DB 접근 전에** 획득.
- 멀티 상품 주문은 productId 정렬 순서로 획득해 데드락 회피.
- 동시성 테스트는 플래키를 피하기 위해 barrier, bounded timeout, deterministic seed를 사용.

---

## 목표

### 핵심 목표
동일 상품에 300건 이상 동시 주문이 들어와도 DB/Hikari 타임아웃이 발생하지 않게 하되, 오버셀을 방지하고(Hikari 설정을 건드리지 않고) 로직/트랜잭션으로 해결한다.

### 완료 정의(DoD)
- 레포 내 테스트가 `POST /api/v1/order`를 동일 상품 대상으로 300 동시 요청으로 실행할 수 있고, 일관되게 종료된다.
- 수정 후에는 “DB 타임아웃 계열” 실패(Hikari 커넥션 획득 타임아웃, MySQL lock wait timeout 등)가 발생하지 않는다.
- 재고는 음수가 되지 않고, 최종 재고는 `initialStock - successCount * quantity` 와 일치한다.

### 가드레일(하면 안 됨)
- HikariCP 사이즈/타임아웃 조정은 **주 해결책으로 사용하지 않기**.
- 단일 인스턴스 전제이므로 Redis/Kafka 같은 신규 인프라는 추가하지 않기.
- 정합성(오버셀 방지, 부분 주문/부분 차감 방치)을 약화시키지 않기.

---

## 검증 전략(필수)

### 테스트 결정
- **테스트 인프라**: 있음(Gradle + JUnit5 + Spring Boot Test). 다만 현재 테스트는 최소 수준.
- **자동화 테스트**: Tests-after.
- **주요 검증**: MockMvc 동시성 테스트 + 3회 반복으로 플래키 확인.

### 에이전트 실행 QA 시나리오(전역)

시나리오: ecommerce_test 스키마 생성
  Tool: Bash (mysql)
  Preconditions: 로컬 MySQL 접속 가능
  Steps:
    1. `mysql -uroot -p1234 -e "CREATE DATABASE IF NOT EXISTS ecommerce_test;"`
    2. `mysql -uroot -p1234 -e "SHOW DATABASES LIKE 'ecommerce_test';"`
  Expected Result: `ecommerce_test` 존재
  Evidence: stdout 를 `.sisyphus/evidence/mysql-create-schema.txt` 로 캡처

시나리오: 베이스라인 재현(수정 전)
  Tool: Bash (Gradle)
  Preconditions: ecommerce_test 존재
  Steps:
    1. `./gradlew test --tests "*Order*Concurrency*" -Dspring.profiles.active=test`
  Expected Result: 현 코드에서 실패가 재현되거나(타임아웃/대량 에러), 최소한 에러 타입 분류가 가능한 로그/출력이 남는다.
  Evidence: Gradle 출력을 `.sisyphus/evidence/concurrency-baseline.txt` 로 캡처

시나리오: 수정 후 green run(안정성)
  Tool: Bash (Gradle)
  Preconditions: 수정 적용 완료
  Steps:
    1. 3회 반복 실행:
       `./gradlew test --tests "*Order*Concurrency*" -Dspring.profiles.active=test`
  Expected Result: 3회 모두 PASS
  Evidence: `.sisyphus/evidence/concurrency-green-run-{1..3}.txt`

---

## 실행 전략

Wave 1 (재현 하네스)
- `ecommerce_test`를 가리키는 test profile 추가
- MockMvc 300 동시성 테스트(시드 데이터 + principal 주입) 추가

Wave 2 (수정)
- 주문 진입점에 상품별(per-product) 벌크헤드/스로틀링 추가(300개 스레드가 동시에 DB/트랜잭션에 진입하지 않도록)
- 트랜잭션 내부 작업을 단축해 락 홀드 시간/커넥션 점유 시간 감소

---

## TODOs

> 메모
> - 이 프로젝트는 멀티 상품 주문을 지원(`OrderRequest.itemInfoList`)하므로, 벌크헤드 획득은 다수 productId에 대해 안전해야 한다.
> - MySQL 조건부 원자 UPDATE를 정합성 앵커로 유지하고, Hikari 설정 변경은 피한다.

### 0) ecommerce_test용 test profile 작성

**할 일**:
- MySQL 스키마 `ecommerce_test`를 바라보는 test profile 설정 작성(로컬 계정/비밀번호 재사용)
- 테스트 반복 실행 재현성을 위해 `spring.jpa.hibernate.ddl-auto` 테스트 친화 값으로 설정(수동 정리 최소화를 위해 `create-drop` 선호)

**추천 에이전트 프로필**:
- Category: `quick`
- Skills: 없음

**레퍼런스**:
- `src/main/resources/application.yml` - 현 datasource 설정을 test profile에 반영

**Acceptance Criteria (에이전트 실행 가능)**:
- `./gradlew test -Dspring.profiles.active=test` 가 `ecommerce_test`로 컨텍스트를 올린다(스키마 생성 외 수동 작업 없음)

### 1) MockMvc 300 동시 주문 테스트 작성(JMeter 제거)

**할 일**:
- MockMvc 기반 Spring Boot 통합 테스트 작성
- `ecommerce_test`에 deterministic seed 데이터 생성
  - BUYER User 1명(`UserPrincipal.userId`로 사용)
  - SELLER User + Brand(상품 생성에 필요)
  - 충분히 큰 `stock`을 가진 Product(300개 모두 성공하도록)
- 컨트롤러 시그니처와 호환되는 principal 주입 로직 작성
  - `src/main/kotlin/small/ecommerce/domain/auth/dto/UserPrincipal.kt` 의 `UserPrincipal(userId, userRole)`
  - Spring Security test request post-processor로 `UsernamePasswordAuthenticationToken` 주입
- 동일 productId에 대해 quantity=1로 300개의 `POST /api/v1/order` 동시 발사
- start barrier 적용(시작 시점 정렬), status/예외 타입/총 소요시간 수집
- 아래 검증 로직 작성
  - 200 응답 300개
  - 타임아웃 계열 예외 0개(발생 시 분류 요약 출력 후 실패)
  - 최종 재고 300 감소

**추천 에이전트 프로필**:
- Category: `unspecified-high`
- Skills: 없음

**레퍼런스**:
- `src/main/kotlin/small/ecommerce/api/OrderController.kt` - 엔드포인트 경로, principal 타입
- `src/main/kotlin/small/ecommerce/domain/order/dto/OrderRequest.kt` - 요청 JSON 형태
- `src/main/kotlin/small/ecommerce/auth/config/SecurityConfig.kt` - 인증 필요, CSRF 비활성화
- `src/main/kotlin/small/ecommerce/domain/auth/dto/UserPrincipal.kt` - principal 클래스
- `src/main/kotlin/small/ecommerce/domain/user/UserRepository.kt` 및 `src/main/kotlin/small/ecommerce/domain/user/User.kt` - User 시드
- `src/main/kotlin/small/ecommerce/domain/Brand/BrandRepository.kt` 및 `src/main/kotlin/small/ecommerce/domain/Brand/Brand.kt` - Brand 시드
- `src/main/kotlin/small/ecommerce/domain/product/Product.kt` - Product 생성 필수 필드

**Acceptance Criteria (에이전트 실행 가능)**:
- `./gradlew test --tests "*Order*Concurrency*" -Dspring.profiles.active=test` 가 안정적으로 종료(무한 대기 없음)
- 테스트 로그에 success/failure(상태코드/예외 타입별)와 총 소요시간이 출력된다

### 2) 동일 상품 스파이크용 상품별(per-product) 벌크헤드(세마포어) 구현(단일 인스턴스)

**목표**: 300개의 요청 스레드가 동시에 DB/트랜잭션에 진입해 Hikari 커넥션 풀을 고갈시키거나 락 대기 폭증을 유발하는 것을 막는다.

**할 일**:
- 상품별(per-product) 인메모리 동시성 제한자(벌크헤드/세마포어) 구현
- 트랜잭션 DB 작업 시작 전에 벌크헤드 획득하도록 흐름 수정
- 적용 위치: 오케스트레이션 레이어(예: `OrderService.createOrder`)에서 `OrderCommander` 호출 전
- 멀티 상품 주문 지원을 위한 안전장치 작성
  - productId 오름차순으로 permits 획득(데드락 회피)
  - `finally`에서 전량 반납
- 상품별 permit 기본값 보수적으로 설정(예: 5)

**추천 에이전트 프로필**:
- Category: `unspecified-high`
- Skills: 없음

**레퍼런스**:
- `src/main/kotlin/small/ecommerce/domain/order/OrderService.kt` - 오케스트레이션/재시도 래퍼
- `src/main/kotlin/small/ecommerce/domain/order/OrderCommander.kt` - 트랜잭션 작업 진입점
- `src/main/kotlin/small/ecommerce/domain/order/dto/OrderRequest.kt` - 멀티 아이템 구조
- `src/main/kotlin/small/ecommerce/domain/product/ProductRepository.kt` - 재고 차감 핫스팟

**Acceptance Criteria (에이전트 실행 가능)**:
- `./gradlew test --tests "*Order*Concurrency*" -Dspring.profiles.active=test` PASS(DB/Hikari 타임아웃 없음)
- 부하 테스트 중 실패가 있다면 비즈니스 실패(재고 부족 등)만 존재하고, 인프라 타임아웃/500이 없어야 한다

### 3) 트랜잭션 작업 단축(락 홀드/커넥션 점유 최소화) 적용

**할 일(최소 변경으로 효과 큰 것부터)**:
- 재고 UPDATE로 row lock 획득 후 커밋까지 시간 최소화하도록 트랜잭션 바디 정리
- 트랜잭션 내부 불필요 SELECT/무거운 처리 제거 또는 이동
- (안전할 경우) 주문 아이템 구성 시 Product 전체 로딩 최소화(JPA reference/proxy 활용 등)

**추천 에이전트 프로필**:
- Category: `unspecified-high`
- Skills: 없음

**레퍼런스**:
- `src/main/kotlin/small/ecommerce/domain/order/OrderCommander.kt` - 트랜잭션 바디
- `src/main/kotlin/small/ecommerce/domain/product/ProductService.kt` - soldProduct 흐름
- `src/main/kotlin/small/ecommerce/domain/product/ProductRepository.kt` - 원자 UPDATE

**Acceptance Criteria (에이전트 실행 가능)**:
- `./gradlew test -Dspring.profiles.active=test` PASS
- 동시성 테스트 3회 연속 PASS

---

## 커밋 전략
- Commit 1: `ecommerce_test` test profile + MockMvc 동시성 테스트 추가
- Commit 2: 벌크헤드/스로틀링 + 트랜잭션 단축(행동 변화는 “타임아웃 제거”로 제한)

---

## 성공 기준

### 검증 커맨드
```bash
mysql -uroot -p1234 -e "CREATE DATABASE IF NOT EXISTS ecommerce_test;"

./gradlew test --tests "*Order*Concurrency*" -Dspring.profiles.active=test

for i in 1 2 3; do ./gradlew test --tests "*Order*Concurrency*" -Dspring.profiles.active=test; done
```

### 최종 체크리스트
- [ ] 동일 상품 300 동시 주문 재현 테스트가 레포에 있고 실행이 쉽다
- [ ] 테스트에서 DB/Hikari 타임아웃이 발생하지 않는다
- [ ] 오버셀 없음: 최종 재고가 기대값과 일치한다
- [ ] HikariCP 튜닝을 주 해결책으로 사용하지 않았다
