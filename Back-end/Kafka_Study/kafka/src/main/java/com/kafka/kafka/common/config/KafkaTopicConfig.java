package com.kafka.kafka.common.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * 카프카 토픽 설정
 * <p>
 * 이 클래스는 카프카 토픽을 생성하고 설정하는 역할을 합니다.
 * Spring Kafka의 TopicBuilder를 사용하여 토픽 속성을 정의합니다.
 * </p>
 */
//@Configuration
public class KafkaTopicConfig {

//    @Bean
//    public NewTopic demoTopic() {
//        return TopicBuilder.name("demo-topic")
//                .partitions(3)
//                .replicas(1)
//                .compact()  // 로그 압축 정책
//                .build();
//    }
}
