package com.finalyearproject.notification_service.dto;

import com.finalyearproject.notification_service.model.NotificationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {

    private String id;
    private String orderId;
    private String customerId;
    private String message;
    private NotificationStatus status;
    private LocalDateTime createdAt;

}
