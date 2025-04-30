package com.kafka.kafka.api;

import com.kafka.kafka.api.dto.KafkaMessage;
import com.kafka.kafka.modules.application.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducerService producerService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        producerService.sendMessage("demo-topic", message);
        return ResponseEntity.ok("Message sent: " + message);
    }

    @PostMapping("/send-json")
    public ResponseEntity<String> sendJsonMessage(@RequestBody KafkaMessage message) {
        message.setTimestamp(LocalDateTime.now());
        producerService.sendJsonMessage("json-topic", message);
        return ResponseEntity.ok("JSON message sent: " + message);
    }
}
