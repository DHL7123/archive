package com.SpringBatch.SpringBatch.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FirstSchedule {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Scheduled(cron = "10 * * * * *", zone = "Asia/Seoul")
    public void runFirstJob() throws Exception {

        log.info("First Schedule Start");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(new Date());

//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("date", date)
//                .toJobParameters();
//        jobLauncher.run(jobRegistry.getJob("firstJob"), jobParameters);


        jobLauncher.run(jobRegistry.getJob("firstJob"), new JobParametersBuilder()
                .addString("date", date)
                .toJobParameters());
    }

}

