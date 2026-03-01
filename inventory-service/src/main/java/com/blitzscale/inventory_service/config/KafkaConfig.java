package com.blitzscale.inventory_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    public static final String ORDER_TOPIC = "order-placed-topic";

    @Bean
    public NewTopic orderPlacedTopic() {
        return TopicBuilder.name(ORDER_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
