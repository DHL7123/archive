package com.SpringBatch.SpringBatch.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @GetMapping("/first")
    public ResponseEntity<String> firstApi(@RequestParam("value") String value) throws Exception {

        jobLauncher.run(jobRegistry.getJob("firstJob"), new JobParametersBuilder()
                .addString("date", value)
                .toJobParameters());

//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("date", value)
//                .toJobParameters();
//
//        jobLauncher.run(jobRegistry.getJob("firstJob"), jobParameters);

        return ResponseEntity.ok("first");
    }

    @GetMapping("/second")
    public ResponseEntity<String> secondApi(@RequestParam("value") String value) throws Exception {

        jobLauncher.run(jobRegistry.getJob("secondJob"), new JobParametersBuilder()
                .addString("date", value)
                .toJobParameters());

        return ResponseEntity.ok("second");
    }
}
