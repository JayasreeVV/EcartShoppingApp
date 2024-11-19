package com.ecart.order_service.publisher;

import com.ecart.order_service.event.OrderPlacedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {
    @Value("${order.event.topicName}")
    private String topic;
    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void sendOrderPlacedEvent(OrderPlacedEvent orderPlacedEvent){
        kafkaTemplate.send(topic, orderPlacedEvent);
    }
}
