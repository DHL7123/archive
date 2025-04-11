# 테이블 배치 작업

스프링 배치를 사용하여 테이블 데이터를 처리하는 두 가지 작업을 정의합니다:
1. **테이블 → 테이블 배치**: 한 테이블의 데이터를 다른 테이블로 이동.
2. **테이블 조건 배치**: 조건에 따라 데이터를 변경 후 저장.

---

## 테이블 생성
- [`Entity`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/entity)
- [`Repository`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/repository)

---

## 배치 소스코드
- [`FirstBatch`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/batch/FirstBatch.java) (테이블 → 테이블 배치)
- [`SecondBatch`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/batch/SecondBatch.java) (테이블 조건 배치)

---

## 청크(Chunk) 처리

- **청크 단위 처리**:  
  데이터를 읽기 → 처리 → 쓰기 작업으로 나누어 청크 단위로 진행합니다.  
  적절한 청크 크기를 설정해야 합니다.  
  - **너무 작을 경우**: I/O 처리가 많아져 오버헤드가 발생합니다.  
  - **너무 클 경우**: 자원 사용 비용이 증가하고, 실패 시 부담이 커집니다.

---

## 작업별 세부 내용

### 1. 테이블 → 테이블 배치

- **설명**:  
  `BeforeEntity` 테이블에서 데이터를 읽어와 `AfterEntity` 테이블로 이동합니다.

- **Processor**:  
  데이터를 단순히 이동하며, 추가적인 변환 작업은 없습니다.

```java
@Bean
public ItemProcessor<BeforeEntity, AfterEntity> middleProcessor() {
    return item -> {
        AfterEntity afterEntity = new AfterEntity();
        afterEntity.setUsername(item.getUsername());
        return afterEntity;
    };
}
```

---

### 2. 테이블 조건 배치

- **설명**:  
  `WinEntity` 테이블에서 데이터를 읽어와 조건에 따라 데이터를 변경한 후 다시 저장합니다.  
  예: `win` 컬럼 값이 10을 초과하면 `reward` 컬럼 값을 `true`로 설정.

- **Processor**:  
  조건에 따라 데이터를 변경합니다.

```java
 @Bean
    public ItemProcessor<WinEntity, WinEntity> trueProcessor() {
        return item -> {
            item.setReward(true);
            return item;
        };
    }
```

---

## 배치 실행

`application.properties`에서 배치 자동 실행을 비활성화(`false`)로 설정했기 때문에, 배치를 실행하기 위해 `JobLauncher`를 구현해야 합니다.

- [`MainController`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/controller/MainController.java)
- [`FirstSchedule`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/schedule/FirstSchedule.java)

`JobLauncher`로 Job을 실행할 때 `JobParameter`를 사용하는 이유는 다음과 같습니다:
- 실행한 작업에 대한 **일자**와 **순번**을 부여합니다.
- 동일한 일자에 대한 작업 수행 여부를 확인하여 **중복 실행**과 **미실행**을 예방합니다.