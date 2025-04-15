package com.SpringBatch.SpringBatch.batch;

import com.SpringBatch.SpringBatch.entity.WinEntity;
import com.SpringBatch.SpringBatch.repository.WinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
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
@SpringBootTest
@ActiveProfiles("test")
class SecondBatchTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Job secondJob;

    @Autowired
    private WinRepository winRepository;

    @BeforeEach
    public void setUp() {
        jobLauncherTestUtils.setJob(secondJob);
    }

    @Test
    @DisplayName("SecondBatch Job Test")
    void testSecondBatchJob() throws Exception {
        // Given: WinRepository에 테스트 데이터를 삽입
        WinEntity winEntity1 = new WinEntity();
        winEntity1.setUsername("User1");
        winEntity1.setWin(15L);
        winEntity1.setReward(false);
        winRepository.save(winEntity1);

        WinEntity winEntity2 = new WinEntity();
        winEntity2.setUsername("User2");
        winEntity2.setWin(5L);
        winEntity2.setReward(false);
        winRepository.save(winEntity2);

        WinEntity winEntity3 = new WinEntity();
        winEntity3.setUsername("User3");
        winEntity3.setWin(20L);
        winEntity3.setReward(false);
        winRepository.save(winEntity3);

        // When: 배치 작업 실행
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // Then: 작업이 성공적으로 완료되었는지 확인
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        // reward가 true로 업데이트되었는지 확인
        List<WinEntity> winEntities = winRepository.findAll();
        assertThat(winEntities).hasSize(3);

        assertThat(winEntities.get(0).isReward()).isTrue(); // win >= 10
        assertThat(winEntities.get(1).isReward()).isFalse(); // win < 10
        assertThat(winEntities.get(2).isReward()).isTrue(); // win >= 10
    }
}