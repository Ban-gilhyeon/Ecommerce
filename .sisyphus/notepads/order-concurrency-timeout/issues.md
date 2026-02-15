# 이슈/리스크

- 작성
- 로컬 환경에 `mysql` CLI가 없어 `CREATE DATABASE ecommerce_test` 명령 자동 실행 불가 확인함
- 테스트 실행 시 `Unknown database 'ecommerce_test'`로 컨텍스트 부팅 실패 확인함
- Kotlin LSP 미설치로 LSP 진단 기반 정적 검증 불가(대신 Gradle 컴파일 검증 수행)
- 현재 레포에 전역 예외 매핑(`@RestControllerAdvice`)이 없어 fail-fast/혼잡 제어용 전용 에러코드 도입 시 응답 형태 일관성 리스크 존재함
