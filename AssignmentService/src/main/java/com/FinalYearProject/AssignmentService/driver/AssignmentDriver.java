package com.FinalYearProject.AssignmentService.driver;

import com.FinalYearProject.AssignmentService.dto.AgentRequest;
import com.FinalYearProject.AssignmentService.dto.AgentResponse;
import com.FinalYearProject.AssignmentService.dto.AssignmentRequest;
import com.FinalYearProject.AssignmentService.dto.AssignmentResponse;
import com.FinalYearProject.AssignmentService.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentDriver {

    private final AssignmentService assignmentService;

    @PostMapping("/assign")
    public ResponseEntity<AssignmentResponse> assignOrder(
            @Valid @RequestBody AssignmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assignmentService.assignOrder(request));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<AssignmentResponse> getByOrder(
            @PathVariable String orderId) {
        return ResponseEntity.ok(assignmentService.getAssignmentByOrder(orderId));
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<AssignmentResponse>> getByAgent(
            @PathVariable String agentId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByAgent(agentId));
    }

    @PatchMapping("/order/{orderId}/delivered")
    public ResponseEntity<AssignmentResponse> markDelivered(
            @PathVariable String orderId) {
        return ResponseEntity.ok(assignmentService.markDelivered(orderId));
    }

    @PatchMapping("/order/{orderId}/failed")
    public ResponseEntity<AssignmentResponse> markFailed(
            @PathVariable String orderId,
            @RequestParam String reason) {
        return ResponseEntity.ok(assignmentService.markFailed(orderId, reason));
    }

    @PostMapping("/agents")
    public ResponseEntity<AgentResponse> registerAgent(
            @Valid @RequestBody AgentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assignmentService.registerAgent(request));
    }

    @GetMapping("/agents/available")
    public ResponseEntity<List<AgentResponse>> getAvailableAgents() {
        return ResponseEntity.ok(assignmentService.getAvailableAgents());
    }
}
