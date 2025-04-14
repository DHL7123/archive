package com.SpringBatch.SpringBatch.config;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@TestConfiguration
@Profile("test")
public class BatchTestConfig {


    // 테스트용 데이터소스 빈 추가
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-meta")
    public DataSource testMetaDataSource() {
        return DataSourceBuilder.create().build();
    }


}
