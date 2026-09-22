package com.finalyearproject.notification_service.kafka;

import com.finalyearproject.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventForCustomers {

    private final NotificationService notificationService;

    @KafkaListener(topics = "order.created", groupId = "notification-group")
    public void handleOrderCreated(String payload) {
        log.info("Received order.created event: {}", payload);
        try {
            String orderId = extractValue(payload, "orderId");
            String customerId = extractValue(payload, "customerId");
            notificationService.sendNotification(
                    orderId,
                    customerId,
                    "Your order has been received and is being processed."
            );
        } catch (Exception e) {
            log.error("Failed to process order.created event: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "delivery.assigned", groupId = "notification-group")
    public void handleDeliveryAssigned(String payload) {
        log.info("Received delivery.assigned event: {}", payload);
        try {
            String orderId = extractValue(payload, "orderId");
            String agentId = extractValue(payload, "agentId");
            notificationService.sendNotification(
                    orderId,
                    "customer-unknown",
                    "A delivery agent has been assigned to your order. Agent ID: " + agentId
            );
        } catch (Exception e) {
            log.error("Failed to process delivery.assigned event: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "delivery.failed", groupId = "notification-group")
    public void handleDeliveryFailed(String payload) {
        log.info("Received delivery.failed event: {}", payload);
        try {
            String orderId = extractValue(payload, "orderId");
            String reason = extractValue(payload, "reason");
            notificationService.sendNotification(
                    orderId,
                    "customer-unknown",
                    "Your delivery has failed. Reason: " + reason
            );
        } catch (Exception e) {
            log.error("Failed to process delivery.failed event: {}", e.getMessage());
        }
    }

    private String extractValue(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search) + search.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}


