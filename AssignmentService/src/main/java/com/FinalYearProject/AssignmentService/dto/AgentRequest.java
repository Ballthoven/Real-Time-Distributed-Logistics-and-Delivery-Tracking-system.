package com.FinalYearProject.AssignmentService.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AgentRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone is required")
    private String phone;
}
