package com.finalyearProject.TrackingService.dto;

import com.finalyearProject.TrackingService.model.LocationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LocationUpdateResponse {
    private String agentId;
    private String id;
    private String orderId;
    private Double latitude;
    private Double longitude;
    private LocationStatus locationStatus;
    private LocalDateTime timestamp;
}
