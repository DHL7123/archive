package com.SpringBatch.SpringBatch.batch;

import com.SpringBatch.SpringBatch.config.BatchTestConfig;
import com.SpringBatch.SpringBatch.config.TestDBConfig;
import com.SpringBatch.SpringBatch.entity.AfterEntity;
import com.SpringBatch.SpringBatch.entity.BeforeEntity;
import com.SpringBatch.SpringBatch.repository.AfterRepository;
import com.SpringBatch.SpringBatch.repository.BeforeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest(classes = {BatchTestConfig.class, TestDBConfig.class})
@ActiveProfiles("test")
public class FirstBatchTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private BeforeRepository beforeRepository;

    @Autowired
    private AfterRepository afterRepository;

    @Test
    public void testFirstBatchJob() throws Exception {
        // Given: BeforeRepository에 테스트 데이터를 삽입
        BeforeEntity beforeEntity1 = new BeforeEntity();
        beforeEntity1.setUsername("User1");
        beforeRepository.save(beforeEntity1);

        BeforeEntity beforeEntity2 = new BeforeEntity();
        beforeEntity2.setUsername("User2");
        beforeRepository.save(beforeEntity2);

        // When: 배치 작업 실행
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // Then: 작업이 성공적으로 완료되었는지 확인
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        // 데이터가 처리되어 AfterRepository에 저장되었는지 확인
        List<AfterEntity> afterEntities = afterRepository.findAll();
        assertThat(afterEntities).hasSize(2);
        assertThat(afterEntities.get(0).getUsername()).isEqualTo("User1");
        assertThat(afterEntities.get(1).getUsername()).isEqualTo("User2");
    }
}