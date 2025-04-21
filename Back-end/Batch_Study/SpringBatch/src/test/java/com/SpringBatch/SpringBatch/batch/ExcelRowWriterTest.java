package com.SpringBatch.SpringBatch.batch;

import com.SpringBatch.SpringBatch.entity.BeforeEntity;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;

import java.io.File;
import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;

class ExcelRowWriterTest {

    @Test
    @DisplayName("ExcelRowWriter Test")
    void testExcelRowWriter() throws Exception {
        // Given: 테스트용 파일 경로와 ExcelRowWriter 생성
        String testFilePath = "src/test/resources/test_output.xlsx";
        ExcelRowWriter writer = new ExcelRowWriter(testFilePath);

        // ExcelRowWriter 열기
        writer.open(new ExecutionContext());

        // 테스트 데이터 작성
        BeforeEntity entity1 = new BeforeEntity();
        entity1.setUsername("User1");
        BeforeEntity entity2 = new BeforeEntity();
        entity2.setUsername("User2");

        writer.write(new Chunk<>(java.util.List.of(entity1, entity2)));

        // ExcelRowWriter 닫기
        writer.close();

        // Then: Excel 파일 내용 검증
        try (FileInputStream fis = new FileInputStream(new File(testFilePath))) {
            var workbook = WorkbookFactory.create(fis);
            var sheet = workbook.getSheetAt(0);

            assertThat(sheet.getRow(0).getCell(0).getStringCellValue()).isEqualTo("User1");
            assertThat(sheet.getRow(1).getCell(0).getStringCellValue()).isEqualTo("User2");
        }
    }
}