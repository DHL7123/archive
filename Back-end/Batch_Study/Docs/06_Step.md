# Step

Step은 배치 작업을 처리하는 하나의 묶음으로, 두 가지 방식으로 구현됩니다:
1. **Chunk 방식 처리**: 데이터를 읽기(Read) → 처리(Process) → 쓰기(Write)로 나누어 처리합니다.
2. **Tasklet 방식 처리**: 단순한 작업(예: 파일 삭제, 값 초기화)을 처리할 때 사용됩니다.

---

## Chunk 단위 처리 과정

Chunk 단위 처리의 기본 흐름은 다음과 같습니다:
- **청크 크기**: 청크 크기를 10으로 설정한 경우,  
  `(Read) X 10 → (Process) X 10 → Write` 순서로 처리됩니다.

---

## 주요 설정

### 1. Skip

Skip은 Step 실행 중 특정 예외가 발생했을 때, 해당 예외를 건너뛰고 작업을 계속 진행하도록 설정합니다.

```java
@Bean
public Step fifthStep() {
    return new StepBuilder("fifthStep", jobRepository)
            .<BeforeEntity, AfterEntity>chunk(10, platformTransactionManager)
            .reader(beforefifthReader())
            .processor(middlefifthProcessor())
            .writer(afterfifthWriter())
            .faultTolerant()
            .skip(Exception.class) // 특정 예외를 건너뜀
            .noSkip(FileNotFoundException.class) // 특정 예외는 건너뛰지 않음
            .skipLimit(10) // 최대 10번까지 Skip 허용
            .build();
}
```

- **Skip 순서**: `skip`과 `noSkip`의 순서는 무관합니다.

#### 커스텀 Skip 설정

모든 예외를 허용하거나, 특정 조건에 따라 Skip을 설정하려면 `SkipPolicy`를 구현합니다.

```java
@Configuration
public class CustomSkipPolicy implements SkipPolicy {
    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        return true; // 모든 예외를 Skip
    }
}
```

```java
@Bean
public Step fifthStep() {
    return new StepBuilder("fifthStep", jobRepository)
            .<BeforeEntity, AfterEntity>chunk(10, platformTransactionManager)
            .reader(beforefifthReader())
            .processor(middlefifthProcessor())
            .writer(afterfifthWriter())
            .faultTolerant()
            .skipPolicy(new CustomSkipPolicy()) // 커스텀 Skip 정책 적용
            .build();
}
```

---

### 2. Retry

Retry는 Step 실행 중 특정 예외가 발생했을 때, 해당 작업을 재시도하도록 설정합니다.

```java
@Bean
public Step fifthStep() {
    return new StepBuilder("fifthStep", jobRepository)
            .<BeforeEntity, AfterEntity>chunk(10, platformTransactionManager)
            .reader(beforefifthReader())
            .processor(middlefifthProcessor())
            .writer(afterfifthWriter())
            .faultTolerant()
            .retryLimit(3) // 최대 3번 재시도
            .retry(SQLException.class) // 재시도할 예외
            .retry(IOException.class)
            .noRetry(FileNotFoundException.class) // 재시도하지 않을 예외
            .build();
}
```

---

### 3. Writer 롤백 제어

Writer 실행 중 특정 예외에 대해 트랜잭션 롤백을 제외하려면 `noRollback`을 설정합니다.

```java
@Bean
public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("step1", jobRepository)
            .<String, String>chunk(2, transactionManager)
            .reader(itemReader())
            .writer(itemWriter())
            .faultTolerant()
            .noRollback(ValidationException.class) // ValidationException 발생 시 롤백 제외
            .build();
}
```

---

### 4. Step Listener

Step Listener는 Step 실행 전후에 특정 작업을 수행할 수 있도록 설정합니다.  
예를 들어, 로그를 남기거나 Step 간 의존성을 처리할 때 사용할 수 있습니다.

```java
@Bean
public StepExecutionListener stepExecutionListener() {
    return new StepExecutionListener() {
        @Override
        public void beforeStep(StepExecution stepExecution) {
            // Step 실행 전 작업
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            // Step 실행 후 작업
            return ExitStatus.COMPLETED;
        }
    };
}
```

Step에 Listener를 추가하려면 다음과 같이 설정합니다:

```java
@Bean
public Step fifthStep() {
    return new StepBuilder("fifthStep", jobRepository)
            .<BeforeEntity, AfterEntity>chunk(10, platformTransactionManager)
            .reader(beforefifthReader())
            .processor(middlefifthProcessor())
            .writer(afterfifthWriter())
            .listener(stepExecutionListener()) // Listener 추가
            .build();
}
```

---

## 참고 자료

- [스프링 배치 Skip 공식 문서](https://docs.spring.io/spring-batch/reference/step/chunk-oriented-processing/configuring-skip.html)