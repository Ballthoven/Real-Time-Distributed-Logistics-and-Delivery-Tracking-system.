package com.finalyearProject.OrderService.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalyearProject.OrderService.enums.OrderStatus;
import com.finalyearProject.OrderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryEventConsumer {
    private final OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "delivery.assigned", groupId = "order-service-group")
    public void consumeDeliveryAssigned(String message) {
        try {
            Map<String, String> event = objectMapper.readValue(message, Map.class);
            String orderId = event.get("orderId");
            String agentId = event.get("agentId");

            log.info("Received delivery.assigned for order: {} -> agent: {}", orderId, agentId);
            orderService.assignAgentToOrder(orderId, agentId);
        } catch (Exception e) {
            log.error("Failed to process delivery.assigned event: {}", message, e);
        }
    }
    @KafkaListener(topics = "delivery.completed", groupId = "order-service-group")
    public void consumeDeliveryCompleted(String message) {
        try {
            Map<String, String> event = objectMapper.readValue(message, Map.class);
            String orderId = event.get("orderId");

            log.info("Received delivery.completed for order: {}", orderId);
            orderService.updateOrderStatus(orderId, OrderStatus.DELIVERED);
        } catch (Exception e) {
            log.error("Failed to process delivery.completed event: {}", message, e);
        }
    }
}
