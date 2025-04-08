# DB 설정

## (`application.properties`)

- [`application.properties`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/resources/application.properties)


## 메타데이터 DB와 데이터 DB 분리

1. **메타데이터용 DB**  (`spring.datasource-meta`)
   - 배치 작업의 진행 사항 및 내용을 기록하는 테이블을 위한 DB.

2. **데이터소스용 DB**  (`spring.datasource-data`)
   - 배치 작업에서 처리할 데이터를 저장하는 DB.
   
---


## JDBC URL 파라미터 

<details> 
<summary>자세히 보기</summary>

1. **`useSSL=false`**  
   - **기능**: SSL 암호화 연결 사용 여부.  
   - **개발환경**: `false`로 설정 (간편성 우선).  
   - **운영환경**: 보안을 위해 반드시 `true` 권장.

2. **`useUnicode=true`**  
   - **기능**: 유니코드 문자 처리 활성화.  
   - **필요성**: 한글 등 다국어 데이터 정상 처리.  
   - **문제 방지**: 문자 깨짐 현상 해결.

3. **`serverTimezone=Asia/Seoul`**  
   - **기능**: DB 서버 시간대 강제 지정.  
   - **이슈**: 미설정 시 애플리케이션-DB 간 시간 불일치 발생.

4. **`allowPublicKeyRetrieval=true`**  
   - **대상**: MySQL 8.0+ 인증 방식.  
   - **용도**: 공개 키 검색 허용으로 연결 오류 방지.  
   - **주의**: 보안 약화 가능성 → 운영환경에선 신중히 적용.

</details>

---
## DB 연결 Config

- [`Config`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/config)

스프링 부트에서 2개 이상의 DB를 연결할 때는 각각의 `DataSource`, `EntityManagerFactory`, `TransactionManager`를 설정하는 `Config` 클래스가 필요.  
이 중 **우선 순위를 지정하기 위해 `@Primary` 어노테이션**을 사용하며,  
스프링 배치의 **기본 메타데이터 테이블은 `@Primary`가 적용된 DB에 생성.**

---

## 메타데이터 테이블

스프링 배치에서 중요한 작업을 트래킹하기 위해 메타데이터를 관리한다.  
메타데이터는 DB 테이블에 저장되며, `application.properties`에서 `@Primary`로 설정된 데이터소스에 테이블이 자동 생성된다.

---

## 메타데이터 테이블 생성 설정

```properties
spring.batch.jdbc.initialize-schema=always
```

- **`always` 설정**:  
  배치 설정에서 JDBC를 통해 메타데이터 테이블을 생성한다.  
  연결된 DB에 따라 자동으로 생성 쿼리를 찾는다.

- **커스텀 생성 쿼리**:  
  자동 생성이 실패하거나 커스텀 쿼리를 사용하려면 `classpath` 경로를 지정할 수 있다.

---

## 테이블 ERD

![메타데이터 테이블 ERD](https://docs.spring.io/spring-batch/reference/_images/meta-data-erd.png)  
*(출처: Spring 공식 문서)*

---

## 테이블 스크립트 경로 확인

- **경로**:  
  `External Libraries > Gradle: org.springframework.batch:spring-batch-core:<버전> > org.springframework.batch.core`

- **스크립트 위치**:  
  스프링 배치 코어 라이브러리 내부에 포함된 테이블 생성 스크립트를 확인할 수 있다.