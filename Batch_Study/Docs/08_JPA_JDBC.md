# JPA와 JDBC

JPA와 JDBC는 스프링 배치에서 데이터를 읽고 쓰는 데 사용되는 주요 기술입니다.  
하지만 JPA의 `IDENTITY` 전략은 성능 저하를 유발할 수 있으며, 이를 해결하기 위해 JDBC를 사용하는 것이 유리한 경우가 있습니다.

---

## JPA의 Write 성능 문제

### 문제 정의
JPA를 사용하여 데이터를 쓰는 경우, 특히 `IDENTITY` ID 생성 전략을 사용할 때 성능이 저하될 수 있습니다.

### 성능 저하 원인
- **Bulk Insert 실패**:  
  JPA의 `IDENTITY` 전략은 각 엔티티를 저장할 때마다 DB에서 ID 값을 생성합니다.  
  이로 인해 청크 단위의 Bulk Insert가 불가능해지고, Insert 쿼리가 개별적으로 실행됩니다.
- **JDBC와의 차이점**:  
  - **JPA**: 청크 크기만큼 데이터를 저장하더라도 Insert 쿼리가 개별적으로 실행됩니다.  
  - **JDBC**: 청크 크기만큼 데이터를 모아 한 번의 Bulk Insert로 처리합니다.

---

## 해결 방법: JDBC 사용

JDBC를 사용하면 청크 단위로 데이터를 모아 한 번의 Bulk Insert로 처리할 수 있습니다.  
아래는 JPA와 JDBC를 사용한 Reader와 Writer 구현을 비교한 예제입니다.

---

## 비교 구현

### Reader 구현

#### JPA 기반 Reader

```java
@Bean
public RepositoryItemReader<BeforeEntity> beforeSixthReader() {
    return new RepositoryItemReaderBuilder<BeforeEntity>()
            .name("beforeReader")
            .pageSize(10)
            .methodName("findAll")
            .repository(beforeRepository)
            .sorts(Map.of("id", Sort.Direction.ASC))
            .build();
}
```

#### JDBC 기반 Reader

```java
@Bean
public JdbcPagingItemReader<BeforeEntity> beforeSixthReader() {
    return new JdbcPagingItemReaderBuilder<BeforeEntity>()
            .name("beforeSixthReader")
            .dataSource(dataSource)
            .selectClause("SELECT id, username")
            .fromClause("FROM BeforeEntity")
            .sortKeys(Map.of("id", Order.ASCENDING))
            .rowMapper(new CustomBeforeRowMapper())
            .pageSize(10)
            .build();
}
```

---

### Writer 구현

#### JPA 기반 Writer

```java
@Bean
public RepositoryItemWriter<AfterEntity> afterSixthWriter() {
    return new RepositoryItemWriterBuilder<AfterEntity>()
            .repository(afterRepository)
            .methodName("save")
            .build();
}
```

#### JDBC 기반 Writer

```java
@Bean
public JdbcBatchItemWriter<AfterEntity> afterSixthWriter() {
    String sql = "INSERT INTO AfterEntity (username) VALUES (:username)";

    return new JdbcBatchItemWriterBuilder<AfterEntity>()
            .dataSource(dataSource)
            .sql(sql)
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .build();
}
```

---

## JPA와 JDBC 비교 요약

| 항목                | JPA                                      | JDBC                                     |
|---------------------|------------------------------------------|------------------------------------------|
| **Reader**          | `RepositoryItemReader`                  | `JdbcPagingItemReader`                   |
| **Writer**          | `RepositoryItemWriter`                  | `JdbcBatchItemWriter`                    |
| **Insert 방식**     | 개별 Insert 쿼리 실행                   | 청크 단위 Bulk Insert 실행               |
| **성능**            | 상대적으로 느림                         | 상대적으로 빠름                          |
| **유지보수성**      | 코드가 간결                              | SQL 작성 필요, 코드 복잡성 증가          |

---

## 참고 자료

- [스프링 배치 공식 문서 - Reader](https://docs.spring.io/spring-batch/docs/current/reference/html/readersAndWriters.html#itemReaders)
- [스프링 배치 공식 문서 - Writer](https://docs.spring.io/spring-batch/docs/current/reference/html/readersAndWriters.html#itemWriters)
- [JPA IDENTITY 전략 공식 문서](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#identifiers-generators-identity)