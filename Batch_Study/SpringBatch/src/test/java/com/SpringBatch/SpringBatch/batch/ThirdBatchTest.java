package com.SpringBatch.SpringBatch.batch;

import com.SpringBatch.SpringBatch.entity.AfterEntity;
import com.SpringBatch.SpringBatch.repository.AfterRepository;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
class ThirdBatchTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Job thirdJob;

    @Autowired
    private AfterRepository afterRepository;

    @MockBean
    private ItemStreamReader<Row> thirdReader; // Mocking ExcelRowReader

    @BeforeEach
    public void setUp() {
        jobLauncherTestUtils.setJob(thirdJob);
    }

    @Test
    @DisplayName("ThirdBatch Job Test with Mocking")
    void testThirdBatchJobWithMocking() throws Exception {
        // Given: Mock Row와 Cell 데이터 생성
        Row mockRow1 = Mockito.mock(Row.class);
        Row mockRow2 = Mockito.mock(Row.class);
        org.apache.poi.ss.usermodel.Cell mockCell1 = Mockito.mock(org.apache.poi.ss.usermodel.Cell.class);
        org.apache.poi.ss.usermodel.Cell mockCell2 = Mockito.mock(org.apache.poi.ss.usermodel.Cell.class);

        // Mock Cell 동작 설정
        when(mockCell1.getStringCellValue()).thenReturn("MockUser1");
        when(mockCell2.getStringCellValue()).thenReturn("MockUser2");

        // Mock Row가 Mock Cell을 반환하도록 설정
        when(mockRow1.getCell(0)).thenReturn(mockCell1);
        when(mockRow2.getCell(0)).thenReturn(mockCell2);

        // Mock Row Iterator 생성
        Iterator<Row> mockRowIterator = Arrays.asList(mockRow1, mockRow2).iterator();
        when(thirdReader.read()).thenAnswer(invocation -> mockRowIterator.hasNext() ? mockRowIterator.next() : null);

        // When: 배치 작업 실행
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // Then: 작업이 성공적으로 완료되었는지 확인
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        // 결과 데이터 검증
        List<AfterEntity> afterEntities = afterRepository.findAll();
        assertThat(afterEntities).hasSize(2);
        assertThat(afterEntities.get(0).getUsername()).isEqualTo("MockUser1");
        assertThat(afterEntities.get(1).getUsername()).isEqualTo("MockUser2");
    }
}