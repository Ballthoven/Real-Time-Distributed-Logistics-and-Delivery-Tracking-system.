package com.FinalYearProject.AssignmentService.dto;


import com.FinalYearProject.AssignmentService.model.AssignmentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AssignmentResponse {
    private String id;
    private String orderId;
    private String agentId;
    private AssignmentStatus status;
    private String failureReason;
    private LocalDateTime assignedAt;
    private LocalDateTime updatedAt;
}
