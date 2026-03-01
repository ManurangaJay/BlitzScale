package com.blitzscale.inventory_service.service.impl;

import com.blitzscale.common.event.OrderPlacedEvent;
import com.blitzscale.inventory_service.config.KafkaConfig;
import com.blitzscale.inventory_service.service.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisherImpl implements EventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishOrderEvent(OrderPlacedEvent event) {
        log.info("Publishing OrderPlacedEvent to Kafka for Order ID: {}" , event.getOrderTrachingId());

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
                KafkaConfig.ORDER_TOPIC,
                event.getOrderTrachingId(),
                event
        );

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Successfully sent message for Order ID: {} with offset: {}",
                        event.getOrderTrachingId(), result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send message for Order ID: {} due to : {}",
                        event.getOrderTrachingId(), ex.getMessage());
            }
        });
    }
}
