package com.SpringBatch.SpringBatch.batch;

import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class ExcelRowReaderTest {

    @Test
    @DisplayName("ExcelRowReader Test")
    void testExcelRowReader() throws Exception {
        // Given: 테스트용 Excel 파일 경로
        String testFilePath = "src/test/resources/test_data.xlsx";

        // ExcelRowReader 인스턴스 생성
        ExcelRowReader excelRowReader = new ExcelRowReader(testFilePath);

        // When: ExcelRowReader 열기 및 읽기
        ExecutionContext executionContext = new ExecutionContext();
        excelRowReader.open(executionContext);

        Row row1 = excelRowReader.read();
        Row row2 = excelRowReader.read();
        Row row3 = excelRowReader.read(); // 마지막 행 이후 null 반환

        // Then: 읽은 데이터 검증
        assertThat(row1).isNotNull();
        assertThat(row1.getCell(0).getStringCellValue()).isEqualTo("TestUser1");

        assertThat(row2).isNotNull();
        assertThat(row2.getCell(0).getStringCellValue()).isEqualTo("TestUser2");

        assertThat(row3).isNull();

        // ExcelRowReader 닫기
        excelRowReader.close();
    }
}