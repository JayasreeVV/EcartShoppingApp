package com.ecart.order_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Value("${order.event.topicName}")
    private String topic;

    @Bean
    public NewTopic newTopic(){
        return new NewTopic(topic, 3, (short)1);
    }
}
