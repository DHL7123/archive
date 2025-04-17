# Job

Job은 하나 이상의 Step을 포함하는 배치 작업의 최상위 구성 요소입니다.  
Job은 Step을 순차적으로 실행하거나 조건에 따라 실행 흐름을 제어할 수 있습니다.

---

## Step Flow

### 1. 순차적으로 Step 실행

Step을 순차적으로 실행하려면 `start()` 메서드로 첫 번째 Step을 설정하고, 이후 Step을 `next()`로 연결합니다.

```java
@Bean
public Job footballJob(JobRepository jobRepository) {
    return new JobBuilder("footballJob", jobRepository)
                     .start(playerLoad()) // 첫 번째 Step
                     .next(gameLoad())    // 두 번째 Step
                     .next(playerSummarization()) // 세 번째 Step
                     .build();
}
```

- **특징**:  
  - 앞선 Step이 실패하면 이후 Step은 실행되지 않습니다.

---

### 2. 조건에 따라 Step 실행

Step 실행 조건을 설정하려면 `on()`과 `to()` 메서드를 사용합니다.

```java
@Bean
public Job conditionalJob(JobRepository jobRepository, Step stepA, Step stepB, Step stepC, Step stepD) {
    return new JobBuilder("conditionalJob", jobRepository)
                .start(stepA) // 첫 번째 Step
                .on("*").to(stepB) // 모든 상태에서 stepB로 이동
                .from(stepA).on("FAILED").to(stepC) // stepA가 실패하면 stepC로 이동
                .from(stepA).on("COMPLETED").to(stepD) // stepA가 완료되면 stepD로 이동
                .end() // Job 종료
                .build();
}
```

- **메서드 설명**:  
  - `on(String exitStatus)`: Step의 종료 상태를 기준으로 조건을 설정합니다.
  - `to(Step step)`: 조건에 따라 실행할 다음 Step을 지정합니다.
  - `end()`: Job의 종료를 명시합니다.

---

## Job Listener

`JobListener`는 Job 실행 전후에 특정 작업을 수행할 수 있도록 설정합니다.  
예를 들어, 로그를 남기거나 Job 실행 결과를 처리할 때 사용할 수 있습니다.

### Listener 구현

```java
@Bean
public JobExecutionListener jobExecutionListener() {
    return new JobExecutionListener() {
        @Override
        public void beforeJob(JobExecution jobExecution) {
            // Job 실행 전 작업
            System.out.println("Job 시작: " + jobExecution.getJobInstance().getJobName());
        }

        @Override
        public void afterJob(JobExecution jobExecution) {
            // Job 실행 후 작업
            System.out.println("Job 종료: " + jobExecution.getStatus());
        }
    };
}
```

### Listener 적용

```java
@Bean
public Job sixthBatch(JobRepository jobRepository, Step sixthStep) {
    return new JobBuilder("sixthBatch", jobRepository)
                .start(sixthStep)
                .listener(jobExecutionListener()) // Listener 추가
                .build();
}
```

---

## 참고 자료

- [스프링 배치 Flow 공식 문서](https://docs.spring.io/spring-batch/reference/step/controlling-flow.html)