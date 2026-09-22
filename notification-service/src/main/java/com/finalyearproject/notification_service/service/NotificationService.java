package com.finalyearproject.notification_service.service;


import com.finalyearproject.notification_service.dto.NotificationResponse;
import com.finalyearproject.notification_service.model.Notification;
import com.finalyearproject.notification_service.model.NotificationStatus;
import com.finalyearproject.notification_service.repository.NotificationRepository;
import com.finalyearproject.notification_service.websocket.NotificationWebsocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationWebsocketHandler notificationWebsocketHandler;
    private final NotificationRepository notificationRepository;

    public void sendNotification(String orderId, String customerId, String message) {
        Notification notification = Notification.builder()
                .orderId(orderId)
                .customerId(customerId)
                .message(message)
                .status(NotificationStatus.PENDING)
                .build();

        try {
            notification.setStatus(NotificationStatus.SUCCESS);
            notificationRepository.save(notification);

            String payload = String.format(
                    "{\"orderId\":\"%s\",\"customerId\":\"%s\",\"message\":\"%s\"}",
                    orderId, customerId, message);

            notificationWebsocketHandler.broadcastNotification(payload);
            log.info("Notification sent for order: {}", orderId);

        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            notificationRepository.save(notification);
            log.error("Failed to send notification for order {}: {}", orderId, e.getMessage());
        }
    }

    public List<NotificationResponse> getNotificationsByOrder(String orderId) {
        return notificationRepository.findByOrderId(orderId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<NotificationResponse> getNotificationsByCustomer(String customerId) {
        return notificationRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .orderId(notification.getOrderId())
                .customerId(notification.getCustomerId())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .build();
    }

}
