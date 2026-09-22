package com.finalyearProject.OrderService.dto;

import com.finalyearProject.OrderService.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class OrderResponse {
    private String id;
    private String customerId;
    private String pickupAddress;
    private String deliveryAddress;
    private String packageDescription;
    private OrderStatus status;
    private String assignedAgentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
