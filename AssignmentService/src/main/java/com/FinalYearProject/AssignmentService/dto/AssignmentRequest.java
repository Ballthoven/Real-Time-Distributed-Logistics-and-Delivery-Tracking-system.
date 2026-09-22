package com.FinalYearProject.AssignmentService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignmentRequest {

    @NotBlank(message = "Order ID is required")
    private String orderId;
}
