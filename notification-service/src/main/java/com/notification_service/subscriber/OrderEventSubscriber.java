package com.notification_service.subscriber;

import com.notification_service.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventSubscriber {

    @KafkaListener(topics = "order-placed-event", groupId = "group-id")
    public void subscribeOrderPlacedEvent(OrderPlacedEvent orderPlacedEvent) {
        sendNotification(orderPlacedEvent);
    }

    private void sendNotification(OrderPlacedEvent orderPlacedEvent) {
        log.info("send order placed event" + orderPlacedEvent.getOrderNumber());
    }
}
