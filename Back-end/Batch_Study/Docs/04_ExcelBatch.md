# 엑셀 배치 작업

엑셀 시트에서 데이터를 읽어와 데이터베이스 테이블로 이동시키고, 테이블 데이터를 엑셀로 내보내는 작업입니다.

---

## 테이블 생성
- [`Entity`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/entity)
- [`Repository`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/repository)

---

## 배치 소스코드
- [`ThirdBatch`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/batch/ThirdBatch.java) (엑셀 → 테이블 배치)
- [`FourthBatch`](https://github.com/DHL7123/Archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/batch/FourthBatch.java) (테이블 → 엑셀 배치)

---

## 작업별 세부 내용

### 1. 엑셀 → 테이블 배치

#### Reader: 엑셀 파일에서 데이터 읽기

- **설명**:  
  `ExcelRowReader`를 사용하여 엑셀 파일의 데이터를 읽어옵니다.

- **Reader 구현**:  
  아래 코드는 엑셀 파일을 읽어오는 `ItemStreamReader`를 정의합니다.

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

#### Processor: 데이터 변환

- **설명**:  
  엑셀 파일에서 읽어온 `Row` 데이터를 `AfterEntity`로 변환합니다.

- **Processor 구현**:  
  데이터를 변환하는 `ItemProcessor`를 정의합니다.

```java
@Bean
public ItemProcessor<Row, AfterEntity> thirdProcessor() {
    return item -> {
        AfterEntity afterEntity = new AfterEntity();
        afterEntity.setUsername(item.getCell(0).getStringCellValue());
        return afterEntity;
    };
}
```

---

### 2. 테이블 → 엑셀 배치

#### Writer: 엑셀 시트 쓰기

- **설명**:  
  `ExcelRowWriter`를 사용하여 테이블 데이터를 엑셀 파일로 작성합니다.

- **Writer 구현**:  
  아래 코드는 데이터를 엑셀 파일로 작성하는 `ItemStreamWriter`를 정의합니다.

```java
@Bean
public ItemStreamWriter<BeforeEntity> excelWriter() {
    try {
        return new ExcelRowWriter("C:\\Users\\A\\Desktop\\result.xlsx");
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
```

- **참고**:  
  [`ExcelRowWriter`](https://github.com/DHL7123/archive/blob/main/Batch_Study/SpringBatch/src/main/java/com/SpringBatch/SpringBatch/batch/ExcelRowWriter.java)

#### Processor: 데이터 변환

- **설명**:  
  테이블 데이터를 그대로 전달하며, 추가적인 변환 작업은 필요하지 않습니다.

```java
@Bean
public ItemProcessor<BeforeEntity, BeforeEntity> fourthProcessor() {
    return item -> item;
}
```

---

## 중요 포인트

### 1. 엑셀 파일의 열림/닫힘 관리

- **문제**:  
  엑셀 파일이 언제 열리고 닫히는지에 따라 성능에 영향을 미칩니다.
  - **Chunk 단위로 매번 열릴지?**  
  - **열린 상태로 유지하며 Chunk 단위로 읽어올지?**

- **정답**:  
  엑셀 파일은 `new`로 생성될 때 한 번 열리고, `open()` 메소드가 호출됩니다. 이후 `read()` 메소드가 호출될 때마다 행 단위로 데이터를 읽어옵니다.  
  파일의 열림/닫힘은 컴퓨터 자원에 큰 부하를 주므로, 구현 시 이를 고려해야 합니다.

---

### 2. 중단점 관리

- **엑셀 → 테이블**:  
  중단점부터 실행하면 효율적입니다.

- **테이블 → 엑셀**:  
  실패 시 파일을 새로 만들어야 하므로, 처음부터 배치를 처리하도록 설정해야 합니다.

| 작업 유형         | 중단점 처리 방식                     |
|------------------|------------------------------------|
| 엑셀 → 테이블    | 중단점부터 재개                     |
| 테이블 → 엑셀    | 처음부터 다시 실행 (파일 새로 생성) |