package com.kafka.kafka.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 설정
 * <p>
 *
 * 이 클래스는 Jackson 라이브러리의 ObjectMapper를 설정합니다.
 * Jackson 라이브러리가 Java 8의 LocalDateTime 타입을 기본으로 지원하지 않아 Java 8의 날짜 및 시간 API를 지원하기 위해 JavaTimeModule을 등록합니다.
 * </p>
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
