package com.finalyearProject.OrderService.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String ORDER_CREATED_TOPIC = "order.created";
    private static final String ORDER_CANCELLED_TOPIC = "order.cancelled";

    public void publishOrderCreated(String orderId, String customerId) {
        String message = String.format(
                "{\"orderId\":\"%s\",\"customerId\":\"%s\"}", orderId, customerId);
        log.info("Publishing order.created event for orderId: {}", orderId);
        kafkaTemplate.send(ORDER_CREATED_TOPIC, orderId, message);
    }

    public void publishOrderCancelled(String orderId) {
        log.info("Publishing order.cancelled event for orderId: {}", orderId);
        kafkaTemplate.send(ORDER_CANCELLED_TOPIC, orderId, orderId);
    }
}