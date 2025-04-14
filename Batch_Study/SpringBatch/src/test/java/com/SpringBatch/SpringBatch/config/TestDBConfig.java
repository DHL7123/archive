package com.SpringBatch.SpringBatch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@TestConfiguration
@Profile("test")
public class TestDBConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource-meta")
    public DataSource testMetaDBSource() {
        return DataSourceBuilder.create().build();
    }
    @Bean
    @Primary
    public PlatformTransactionManager testMetaTransactionManager() {
        return new DataSourceTransactionManager(testMetaDBSource());
    }
}
