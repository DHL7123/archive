package com.kafka.kafka.modules.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.kafka.api.dto.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * 카프카 프로듀서 서비스
 * <p>
 * 이 서비스는 카프카에 메시지를 전송하는 기능을 제공합니다.
 * </p>
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    // 기본 메시지 전송 (비동기)
    public void sendMessage(String topic, String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent successfully to topic: {}, partition: {}, offset: {}, message: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        message);
            } else {
                log.error("Failed to send message: {}", message, ex);
            }
        });
    }

    // JSON 메시지 전송
    public void sendJsonMessage(String topic, KafkaMessage message) {
        try {
            String jsonMessage = convertToJson(message);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, jsonMessage);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("JSON message sent successfully: {}", message);
                } else {
                    log.error("Failed to send JSON message: {}", message, ex);
                }
            });
        } catch (JsonProcessingException e) {
            log.error("Error converting message to JSON: {}", message, e);
        }
    }

    private String convertToJson(KafkaMessage message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(message);
    }
}
