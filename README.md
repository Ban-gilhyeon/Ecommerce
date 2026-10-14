# Ecommerce

Kotlin과 Spring Boot로 구현한 이커머스 백엔드 프로젝트입니다.

회원, 브랜드, 상품, 상품 재고, 쿠폰, 쿠폰 발급, 주문 도메인을 분리하고, JWT 인증과 JPA 기반 CRUD API를 제공합니다. 특히 상품 재고 차감과 주문 처리처럼 동시 요청에서 문제가 발생하기 쉬운 흐름을 도메인 단위로 분리해 확장할 수 있도록 설계했습니다.

## 주요 기능

### 인증 및 회원

- 회원가입
- 로그인 및 JWT Access Token 발급
- Spring Security 기반 Stateless 인증
- 회원 조회, 수정, 삭제
- 이메일 중복 검증
- BCrypt 기반 비밀번호 암호화

### 브랜드

- 브랜드 등록
- 브랜드 목록 및 상세 조회
- 브랜드 수정 및 삭제
- 브랜드 소유자와 회원 도메인 연결
- Seller 권한 기반 브랜드 등록

### 상품 및 재고

- 상품 등록
- 상품 목록, 상세 조회
- 브랜드별 상품 조회
- 상품 수정 및 삭제
- 상품 재고 등록, 조회, 수정, 삭제
- 상품별 재고 조회
- 주문 시 상품 재고 차감
- 재고 부족 시 비즈니스 예외 반환

### 쿠폰

- 쿠폰 등록
- 쿠폰 목록 및 상세 조회
- 쿠폰 수정 및 삭제
- 사용자별 쿠폰 발급 이력 등록
- 쿠폰 발급 이력 조회, 수정, 삭제

### 주문

- 인증된 사용자 기준 주문 생성
- 주문 상품 존재 여부 검증
- 동일 상품의 주문 수량 합산
- 주문 처리 과정에서 상품 재고 차감
- 재고 부족 및 잘못된 상품 ID 예외 처리
- 주문 상태 관리 기반 마련

## 기술 스택

| 구분 | 기술 |
| --- | --- |
| Language | Kotlin 2.2.21 |
| Framework | Spring Boot 4.0.0 |
| Web | Spring MVC |
| Security | Spring Security, JWT, BCrypt |
| ORM | Spring Data JPA, Hibernate |
| Query | QueryDSL 5.1.0 |
| Database | MySQL |
| API 문서 | Springdoc OpenAPI, Swagger UI |
| Build | Gradle |
| Java | JDK 17 |

## 프로젝트 구조

```text
src/main/kotlin/small/ecommerce
├── api
│   ├── AuthController.kt
│   ├── UserController.kt
│   ├── BrandController.kt
│   ├── ProductController.kt
│   ├── CouponController.kt
│   └── OrderController.kt
├── auth
│   ├── config
│   ├── jwt
│   └── CustomUserDetails.kt
├── common
│   ├── exception
│   └── response
└── domain
    ├── auth
    ├── user
    ├── brand
    ├── product
    ├── coupon
    └── order
```

각 도메인은 엔티티, Repository, Service, DTO를 기준으로 분리했습니다. 컨트롤러는 HTTP 요청과 응답 변환을 담당하고, 서비스는 도메인 검증과 유스케이스 처리를 담당합니다.

## API 요약

### Auth

| Method | Endpoint | 설명 |
| --- | --- | --- |
| `POST` | `/api/v1/auth/signup` | 회원가입 |
| `POST` | `/api/v1/auth/login` | 로그인 및 JWT 발급 |

### User

| Method | Endpoint | 설명 |
| --- | --- | --- |
| `POST` | `/api/v1/users` | 회원 생성 |
| `GET` | `/api/v1/users` | 회원 목록 조회 |
| `GET` | `/api/v1/users/{userId}` | 회원 상세 조회 |
| `PUT` | `/api/v1/users/{userId}` | 회원 수정 |
| `DELETE` | `/api/v1/users/{userId}` | 회원 삭제 |

### Brand

| Method | Endpoint | 설명 |
| --- | --- | --- |
| `POST` | `/api/v1/brand/add` | 브랜드 등록 |
| `GET` | `/api/v1/brand/list` | 브랜드 목록 조회 |
| `GET` | `/api/v1/brand/{brandId}` | 브랜드 상세 조회 |
| `PUT` | `/api/v1/brand/{brandId}` | 브랜드 수정 |
| `DELETE` | `/api/v1/brand/{brandId}` | 브랜드 삭제 |

### Product

| Method | Endpoint | 설명 |
| --- | --- | --- |
| `POST` | `/api/v1/products` | 상품 등록 |
| `GET` | `/api/v1/products` | 상품 목록 조회 |
| `GET` | `/api/v1/products/{productId}` | 상품 상세 조회 |
| `GET` | `/api/v1/products/brand/{brandId}` | 브랜드별 상품 조회 |
| `PUT` | `/api/v1/products/{productId}` | 상품 수정 |
| `DELETE` | `/api/v1/products/{productId}` | 상품 삭제 |
| `POST` | `/api/v1/products/stocks` | 상품 재고 등록 |
| `GET` | `/api/v1/products/stocks` | 재고 목록 조회 |
| `GET` | `/api/v1/products/{productId}/stocks` | 상품별 재고 조회 |
| `PUT` | `/api/v1/products/stocks/{stockId}` | 재고 수정 |
| `DELETE` | `/api/v1/products/stocks/{stockId}` | 재고 삭제 |

### Coupon

| Method | Endpoint | 설명 |
| --- | --- | --- |
| `POST` | `/api/v1/coupons` | 쿠폰 등록 |
| `GET` | `/api/v1/coupons` | 쿠폰 목록 조회 |
| `GET` | `/api/v1/coupons/{couponId}` | 쿠폰 상세 조회 |
| `PUT` | `/api/v1/coupons/{couponId}` | 쿠폰 수정 |
| `DELETE` | `/api/v1/coupons/{couponId}` | 쿠폰 삭제 |
| `POST` | `/api/v1/coupons/issues` | 쿠폰 발급 |
| `GET` | `/api/v1/coupons/issues` | 쿠폰 발급 이력 조회 |

### Order

| Method | Endpoint | 설명 |
| --- | --- | --- |
| `POST` | `/api/v1/order` | 인증 사용자 주문 생성 및 재고 차감 |

## 공통 응답 형식

성공과 실패 응답을 공통 포맷으로 반환합니다.

```json
{
  "status": "SUCCESS",
  "payload": {
    "id": 1,
    "name": "테스트상품1"
  },
  "error": null
}
```

예외 발생 시에는 `status`가 `FAIL`로 반환되고, 오류 코드와 메시지는 `error`에 담깁니다.

```json
{
  "status": "FAIL",
  "payload": null,
  "error": {
    "code": "P002",
    "message": "상품의 수량이 부족합니다.",
    "detail": {
      "productId": 1
    }
  }
}
```

## 동시성 관점

이커머스에서 다음 기능은 동시에 여러 요청이 들어올 때 경쟁 조건이 발생하기 쉽습니다.

- 여러 사용자가 같은 상품을 동시에 주문하는 재고 차감
- 같은 쿠폰을 동시에 발급받는 쿠폰 발급
- 재고, 사용자, 쿠폰 상태가 함께 검증되는 주문 생성

현재 프로젝트는 `product`, `product_stock`, `coupon`, `coupon_issue`, `order`를 별도 도메인으로 분리하고, 주문 서비스에서 재고 차감과 주문 저장을 하나의 트랜잭션 흐름으로 처리하는 기반을 구현했습니다. 재고 차감은 Repository의 조건부 업데이트 결과를 검증해 재고 부족 상황을 예외로 처리합니다.

향후 확장 가능한 동시성 제어 방식은 다음과 같습니다.

- JPA 낙관적 락 또는 비관적 락 적용
- 재고 차감 쿼리의 원자성 및 영향 행 검증 강화
- 쿠폰 중복 발급 방지를 위한 유니크 제약 추가
- 동시 주문 시나리오에 대한 통합 테스트 및 부하 테스트

## 실행 방법

### 1. 사전 요구 사항

- JDK 17 이상
- MySQL 8 이상
- 데이터베이스 `ecommerce` 생성

```sql
CREATE DATABASE ecommerce;
```

### 2. 데이터베이스 설정

기본 설정은 `src/main/resources/application.yml`에 있습니다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce
    username: root
    password: 1234
```

로컬 환경에 맞게 사용자명과 비밀번호를 변경하세요.

### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```

애플리케이션은 기본적으로 `http://localhost:8080`에서 실행됩니다.

### 4. 테스트 실행

```bash
./gradlew test
```

테스트 실행에도 MySQL 연결이 필요합니다. 테스트 DB 설정은 `src/test/resources/application-test.yml`을 확인하세요.

## 데이터 초기화

개발 환경에서는 다음 설정으로 Hibernate가 스키마를 생성 또는 갱신하고, 이후 `data.sql`이 실행됩니다.

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
    defer-datasource-initialization: true
  sql:
    init:
      mode: always
```

샘플 데이터는 `src/main/resources/data.sql`에 있으며 회원, 브랜드, 상품, 상품 재고, 쿠폰, 쿠폰 발급 이력 데이터를 포함합니다.

운영 환경에서는 `ddl-auto: update`와 샘플 데이터 자동 삽입을 사용하지 않는 것을 권장합니다.

## API 문서

애플리케이션 실행 후 아래 주소에서 Swagger UI를 확인할 수 있습니다.

```text
http://localhost:8080/swagger-ui/index.html
```

## 현재 범위와 개선 예정

- 결제 도메인은 아직 구현하지 않았습니다.
- 주문 조회, 주문 취소, 주문 상태 변경 API를 확장할 수 있습니다.
- 쿠폰 발급 중복 방지와 발급 수량 제한 정책을 보강할 수 있습니다.
- 재고 차감에 대한 락 전략과 동시성 통합 테스트를 추가할 수 있습니다.
- 개발용 JWT Secret과 DB 접속 정보는 환경 변수 또는 Secret 관리 방식으로 분리할 필요가 있습니다.
