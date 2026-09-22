package com.FinalYearProject.AssignmentService.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class AssignmentEventPublisher {
    @Autowired

    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String DELIVERY_ASSIGNED_TOPIC = "delivery.assigned";
    private static final String DELIVERY_FAILED_TOPIC = "delivery.failed";

    private static final String DELIVERY_COMPLETED_TOPIC = "delivery.completed";

    public void publishDeliveryCompleted(String orderId) {
        String message = String.format("{\"orderId\":\"%s\"}", orderId);
        log.info("Publishing delivery.completed for order: {}", orderId);
        kafkaTemplate.send(DELIVERY_COMPLETED_TOPIC, orderId, message);
    }

    public void publishDeliveryAssigned(String orderId, String agentId) {
            String message = String.format(
                    "{\"orderId\":\"%s\",\"agentId\":\"%s\"}", orderId, agentId);
            log.info("Publishing delivery.assigned for order: {}", orderId);
            kafkaTemplate.send(DELIVERY_ASSIGNED_TOPIC, orderId, message);
    }

    public void publishDeliveryFailed(String orderId, String reason) {
            String message = String.format(
                    "{\"orderId\":\"%s\",\"reason\":\"%s\"}", orderId, reason);
            log.info("Publishing delivery.failed for order: {}", orderId);
            kafkaTemplate.send(DELIVERY_FAILED_TOPIC, orderId, message);
        }

}
