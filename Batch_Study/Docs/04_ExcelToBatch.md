# 엑셀 → 테이블 배치

엑셀 파일의 한 시트에서 데이터를 읽어와 데이터베이스 테이블로 이동시키는 작업입니다.

---

## 테이블 생성
- [`Entity`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/entity/WinEntity.java)
- [`Repository`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/repository/WinRepository.java)

---

## 배치 소스코드
- [`Batch`](https://github.com/DHL7123/archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/batch/ThirdBatch.java)

---

## 작업별 세부 내용

### 1. Reader: 엑셀 파일에서 데이터 읽기

- **설명**:  
  `ExcelRowReader`를 사용하여 엑셀 파일의 데이터를 읽어옵니다.

- **Reader 구현**:  
  엑셀 파일을 읽어오는 `ItemStreamReader`를 정의합니다.

```java
@Bean
public ItemStreamReader<Row> excelReader() {
    try {
        return new ExcelRowReader("C:\\Users\\A\\Desktop\\01_Batch.xlsx");
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
```

- **참고**:  
  [`ExcelRowReader`](https://github.com/DHL7123/archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/batch/ExcelRowReader.java)

---

### 2. Processor: 데이터 변환

- **설명**:  
  엑셀 파일에서 읽어온 `Row` 데이터를 `AfterEntity`로 변환합니다.

- **Processor 구현**:  
 데이터를 변환하는 `ItemProcessor`를 정의합니다.

```java
@Bean
public ItemProcessor<Row, AfterEntity> fourthProcessor() {
    return item -> {
        AfterEntity afterEntity = new AfterEntity();
        afterEntity.setUsername(item.getCell(0).getStringCellValue());
        return afterEntity;
    };
}
```

---

## 중요 포인트

### 1. 엑셀 파일의 열림/닫힘 관리

- **문제**:  
  `ItemStreamReader`에서 엑셀 파일이 언제 열리고 닫히는지에 따라 성능에 영향을 미칩니다.
  - **Chunk 단위로 매번 열릴지?**  
  - **열린 상태로 유지하며 Chunk 단위로 읽어올지?**

- **정답**:  
  엑셀 파일은 `new`로 생성될 때 한 번 열리고, `open()` 메소드가 호출됩니다. 이후 `read()` 메소드가 호출될 때마다 행 단위로 데이터를 읽어옵니다.  
  파일의 열림/닫힘은 컴퓨터 자원에 큰 부하를 주므로, 구현 시 이를 고려해야 합니다.

---

### 2. 중단점 관리

- **문제**:  
  엑셀 데이터를 읽는 도중 프로그램이 멈춘다면, 다시 실행했을 때 중단점부터 작업을 재개할 수 있어야 합니다.

- **해결 방법**:  
  `ExecutionContext`를 활용하여 중단점을 관리합니다. 이를 통해 작업 상태를 저장하고, 재실행 시 저장된 상태부터 작업을 이어갈 수 있습니다.