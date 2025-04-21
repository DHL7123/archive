# ItemStreamReader

`ItemStreamReader`는 배치 작업에서 데이터를 읽는 Reader로, 배치의 상태를 관리하고 데이터를 읽어오는 역할을 합니다.  
스프링 배치에서 제공하는 다양한 Reader 구현체를 사용할 수 있지만, 필요에 따라 커스텀 Reader를 작성해야 할 수도 있습니다.

---

## ItemStreamReader 구성

`ItemStreamReader`는 다음 두 인터페이스를 상속받아 구현됩니다:
- **`ItemStream`**: 배치의 상태를 관리하는 인터페이스.
- **`ItemReader`**: 데이터를 읽어오는 인터페이스.

```java
public interface ItemStreamReader<T> extends ItemStream, ItemReader<T> {
}
```

---

## ItemReader

`ItemReader`는 데이터를 읽어오는 역할을 담당하며, `read()` 메서드를 통해 하나의 데이터를 반환합니다.

```java
@FunctionalInterface
public interface ItemReader<T> {
    @Nullable
    T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException;
}
```

- **`read()` 메서드**:  
  배치 작업 중 데이터를 하나씩 읽어올 때 호출됩니다.

---

## ItemStream

`ItemStream`은 배치 작업의 상태를 관리하기 위한 인터페이스로, 다음 세 가지 메서드를 제공합니다:

```java
public interface ItemStream {
    default void open(ExecutionContext executionContext) throws ItemStreamException {}
    default void update(ExecutionContext executionContext) throws ItemStreamException {}
    default void close() throws ItemStreamException {}
}
```

### 주요 메서드 설명

1. **`open(ExecutionContext executionContext)`**  
   - 배치 작업이 시작될 때 호출됩니다.  
   - 초기화 작업을 수행하거나, 중단점부터 작업을 재개하도록 설정합니다.

2. **`update(ExecutionContext executionContext)`**  
   - `read()` 메서드 호출 후 실행됩니다.  
   - 현재 작업 상태를 기록하는 데 사용됩니다.

3. **`close()`**  
   - 배치 작업이 완료된 후 호출됩니다.  
   - 파일을 닫거나, 사용한 자원을 정리하는 데 사용됩니다.

---

## ExecutionContext

`ExecutionContext`는 배치 작업의 상태를 저장하고 관리하는 객체입니다.  
`ItemStream`의 `open()`과 `update()` 메서드에 매개변수로 전달되며, 작업의 기준점을 추적하는 데 사용됩니다.

### 주요 특징
- **데이터 저장 및 조회**:  
  - `put(String key, Object value)`로 데이터를 저장합니다.  
  - `get(String key)`로 저장된 데이터를 조회합니다.
- **메타데이터 저장**:  
  - `ExecutionContext` 데이터는 `JdbcExecutionContextDao`를 통해 메타데이터 테이블에 저장됩니다.
- **범위**:  
  - `BATCH_JOB_EXECUTION_CONTEXT`: Job 단위로 저장.  
  - `BATCH_STEP_EXECUTION_CONTEXT`: Step 단위로 저장.

### 사용 예시

```java
ExecutionContext context = new ExecutionContext();
context.put("currentIndex", 10);
int index = context.getInt("currentIndex");
```

---

## 구현 예시

- **CustomItemStreamReader**:  
  [CustomItemStreamReader](https://github.com/DHL7123/archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/sample/CustomItemStreamReaderImpl.java)