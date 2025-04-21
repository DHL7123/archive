package com.SpringBatch.SpringBatch.batch;

import com.SpringBatch.SpringBatch.entity.BeforeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@EnableAutoConfiguration
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
class FourthBatchTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Job fourthJob;

    @MockBean(name = "fourthBeforeReader")
    private RepositoryItemReader<BeforeEntity> fourthBeforeReader;

    @MockBean(name = "excelWriter")
    private ItemStreamWriter<BeforeEntity> excelWriter;

    @BeforeEach
    void setUp() {
        jobLauncherTestUtils.setJob(fourthJob);

        // Writer의 open과 close를 Mock으로 설정
        Mockito.doNothing().when(excelWriter).open(Mockito.any());
        Mockito.doNothing().when(excelWriter).close();
    }

    @Test
    @DisplayName("FourthBatch Job Test")
    void testFourthBatchJobWithMocking() throws Exception {
        // Given
        BeforeEntity mockEntity1 = new BeforeEntity();
        mockEntity1.setUsername("MockUser1");
        BeforeEntity mockEntity2 = new BeforeEntity();
        mockEntity2.setUsername("MockUser2");

        when(fourthBeforeReader.read())
                .thenReturn(mockEntity1, mockEntity2, null);

        Mockito.doNothing().when(excelWriter).write(Mockito.any());

        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters()
        );

        // Then
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        // Verify writer was called
        Mockito.verify(excelWriter, Mockito.atLeastOnce()).write(Mockito.any());
        Mockito.verify(excelWriter).open(Mockito.any());
        Mockito.verify(excelWriter).close();
    }
}