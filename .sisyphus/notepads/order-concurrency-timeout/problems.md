# 문제/재현

- 작성
- 재현 커맨드: `./gradlew test --tests "*OrderConcurrencyTest" -Dspring.profiles.active=test`
- 현재 결과: `Unknown database 'ecommerce_test'` 로 실패
- 선행 필요 작업: 로컬 MySQL에 `ecommerce_test` 스키마 생성
