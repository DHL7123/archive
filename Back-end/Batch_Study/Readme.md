#  Spring Batch 5

## Spring Batch 란?
- 일정 시간 동안 대량의 데이터를 한 번에 처리하는 방식을 의미함


- 많은 데이터를 다루는 경우, 중간에 프로그램이 멈출 가능성이 있기 때문에 안전장치가 필요


- 특히 다음과 같은 문제를 해결하기 위해 배치가 필요함:


  - 작업 지점 기록 필요 (프로그램이 중단 되어도 이어서 진행 가능하도록)
  

  - 중복 실행 방지 (오늘 처리한 데이터를 또 처리하지 않도록)


---
## 사용 기술 및 버전
- Java 17
- Spring Boot 3.3.1
- Spring Batch 5.x
- Gradle (Groovy)
- MySQL 
- Lombok

---

## 구현 목표
1. **데이터 베이스 분리 구성**
   - 메타 데이터 DB / 운영 데이터 DB 분리
2. **배치 처리 유형**
   - 테이블 → 테이블 배치
   - 엑셀 → 테이블 배치
   - 테이블 → 엑셀 배치
   - 웹 API → 테이블 배치
3. **실행 방식**
   - 스케줄링 (Cron 표현식 기반)
   - 웹 핸들러 기반 (API 트리거)
