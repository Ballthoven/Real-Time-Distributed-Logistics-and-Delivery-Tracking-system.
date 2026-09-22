package com.finalyearProject.TrackingService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationUpdateRequest {
    @NotBlank(message = "Agent ID is required")
    private String agentId;

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;
}
