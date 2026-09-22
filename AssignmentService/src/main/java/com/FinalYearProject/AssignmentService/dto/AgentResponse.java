package com.FinalYearProject.AssignmentService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgentResponse {
    private String id;
    private String name;
    private String phone;
    private boolean available;
    private Double currentLatitude;
    private Double currentLongitude;
}
