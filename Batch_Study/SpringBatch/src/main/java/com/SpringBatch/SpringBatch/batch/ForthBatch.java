package com.SpringBatch.SpringBatch.batch;


import com.SpringBatch.SpringBatch.entity.BeforeEntity;
import com.SpringBatch.SpringBatch.repository.BeforeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ForthBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final BeforeRepository beforeRepository;

    @Bean
    public Job forthJob() {

        log.info("Forth Job Start");
        return new JobBuilder("forthJob", jobRepository)
                .start(forthStep())
                .build();
    }

    @Bean
    public Step forthStep() {

        log.info("Forth Step Start");

        return new StepBuilder("forthStep", jobRepository)
                .<BeforeEntity, BeforeEntity>chunk(10, platformTransactionManager)
                .reader(forthBeforeReader())
                .processor(forthProcessor())
                .writer(excelWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<BeforeEntity> forthBeforeReader() {

        return new RepositoryItemReaderBuilder<BeforeEntity>()
                .name("beforeReader")
                .pageSize(10)
                .methodName("findAll")
                .repository(beforeRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();

    }

    @Bean
    public ItemProcessor<BeforeEntity, BeforeEntity> forthProcessor() {

        return item -> item;
    }

    @Bean
    public ItemStreamWriter<BeforeEntity> excelWriter() {

        try {
            return new ExcelRowWriter("C:\\Users\\A\\Desktop\\Result.xlsx");
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

