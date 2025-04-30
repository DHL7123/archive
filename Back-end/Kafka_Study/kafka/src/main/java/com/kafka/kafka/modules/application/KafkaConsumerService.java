package com.kafka.kafka.modules.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.kafka.api.dto.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "demo-topic", groupId = "my-group")
    public void consume(String message) {
        log.info("Message received: {}", message);
    }

    @KafkaListener(topics = "json-topic", groupId = "my-group")
    public void consumeJson(String jsonMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            KafkaMessage message = mapper.readValue(jsonMessage, KafkaMessage.class);
            log.info("JSON message received: {}", message);
        } catch (Exception e) {
            log.error("Failed to parse JSON message: {}", jsonMessage, e);
        }
    }


}

