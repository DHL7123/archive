package com.SpringBatch.SpringBatch.batch;


import com.SpringBatch.SpringBatch.entity.AfterEntity;
import com.SpringBatch.SpringBatch.repository.AfterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class ThirdBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final AfterRepository afterRepository;




    @Bean
    public Job thirdJob() {

        log.info("Third Job Start");
        return  new JobBuilder("thirdJob", jobRepository)
                .start(thirdStep())
                .build();
    }

    @Bean
    public Step thirdStep() {

        return new StepBuilder("thirdStep", jobRepository)
                .<Row, AfterEntity> chunk(10,platformTransactionManager)
                .reader(thirdReader())
                .processor(thirdProcessor())
                .writer(thirdWriter())
                .build();
    }

    @Bean
    public ItemStreamReader<Row> thirdReader() {

        try {
            return new ExcelRowReader("C:\\Users\\A\\Desktop\\01_Batch.xlsx");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ItemProcessor<Row, AfterEntity> thirdProcessor() {

        return new ItemProcessor<Row, AfterEntity>() {

            @Override
            public AfterEntity process(Row item) {

                AfterEntity afterEntity = new AfterEntity();
                afterEntity.setUsername(item.getCell(0).getStringCellValue());
                return afterEntity;
            }
        };

    }

    @Bean
    public RepositoryItemWriter<AfterEntity> thirdWriter() {

        return new RepositoryItemWriterBuilder<AfterEntity>()
                .repository(afterRepository)
                .methodName("save")
                .build();
    }
}
