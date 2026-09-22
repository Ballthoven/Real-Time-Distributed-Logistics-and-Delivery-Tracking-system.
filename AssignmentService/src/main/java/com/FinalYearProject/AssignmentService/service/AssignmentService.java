package com.FinalYearProject.AssignmentService.service;

import com.FinalYearProject.AssignmentService.dto.AgentRequest;
import com.FinalYearProject.AssignmentService.dto.AgentResponse;
import com.FinalYearProject.AssignmentService.dto.AssignmentRequest;
import com.FinalYearProject.AssignmentService.dto.AssignmentResponse;
import com.FinalYearProject.AssignmentService.kafka.AssignmentEventPublisher;
import com.FinalYearProject.AssignmentService.model.Assignment;
import com.FinalYearProject.AssignmentService.model.AssignmentStatus;
import com.FinalYearProject.AssignmentService.model.DeliveryAgent;
import com.FinalYearProject.AssignmentService.repository.AssignmentRepository;
import com.FinalYearProject.AssignmentService.repository.DeliveryAgentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final DeliveryAgentRepository agentRepository;
    private final AssignmentEventPublisher eventPublisher;

    public AssignmentResponse assignOrder(AssignmentRequest request) {
        List<DeliveryAgent> availableAgents = agentRepository.findByAvailableTrue();

        if (availableAgents.isEmpty()) {
            log.warn("No available agents for order: {}", request.getOrderId());
            eventPublisher.publishDeliveryFailed(request.getOrderId(), "No available agents");

            Assignment failed = Assignment.builder()
                    .orderId(request.getOrderId())
                    .agentId("UNASSIGNED")
                    .status(AssignmentStatus.FAILED)
                    .failureReason("No available agents")
                    .build();

            return mapToResponse(assignmentRepository.save(failed));
        }

        DeliveryAgent agent = availableAgents.get(0);
        agent.setAvailable(false);
        agentRepository.save(agent);

        Assignment assignment = Assignment.builder()
                .orderId(request.getOrderId())
                .agentId(agent.getId())
                .status(AssignmentStatus.ASSIGNED)
                .build();

        Assignment saved = assignmentRepository.save(assignment);
        eventPublisher.publishDeliveryAssigned(saved.getOrderId(), saved.getAgentId());
        log.info("Order {} assigned to agent {}", request.getOrderId(), agent.getId());
        return mapToResponse(saved);
    }

    public AssignmentResponse getAssignmentByOrder(String orderId) {
        Assignment assignment = assignmentRepository.findTopByOrderIdOrderByAssignedAtDesc(orderId)
                .orElseThrow(() -> new RuntimeException("Assignment not found for order: " + orderId));
        return mapToResponse(assignment);
    }

    public List<AssignmentResponse> getAssignmentsByAgent(String agentId) {
        return assignmentRepository.findByAgentId(agentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AssignmentResponse markDelivered(String orderId) {
        Assignment assignment = assignmentRepository
                .findTopByOrderIdOrderByAssignedAtDesc(orderId)
                .orElseThrow(() -> new RuntimeException("Assignment not found: " + orderId));

        assignment.setStatus(AssignmentStatus.DELIVERED);
        Assignment updated = assignmentRepository.save(assignment);

        DeliveryAgent agent = agentRepository.findById(assignment.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        agent.setAvailable(true);
        agentRepository.save(agent);

        eventPublisher.publishDeliveryCompleted(orderId);
        log.info("Order {} marked as delivered", orderId);
        return mapToResponse(updated);
    }

    public AssignmentResponse markFailed(String orderId, String reason) {
        Assignment assignment = assignmentRepository.findTopByOrderIdOrderByAssignedAtDesc(orderId)
                .orElseThrow(() -> new RuntimeException("Assignment not found: " + orderId));

        assignment.setStatus(AssignmentStatus.FAILED);
        assignment.setFailureReason(reason);
        Assignment updated = assignmentRepository.save(assignment);

        DeliveryAgent agent = agentRepository.findById(assignment.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        agent.setAvailable(true);
        agentRepository.save(agent);

        eventPublisher.publishDeliveryFailed(orderId, reason);
        log.info("Order {} marked as failed: {}", orderId, reason);
        return mapToResponse(updated);
    }

    public AgentResponse registerAgent(AgentRequest request) {
        DeliveryAgent agent = DeliveryAgent.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .available(true)
                .build();
        DeliveryAgent saved = agentRepository.save(agent);
        log.info("New agent registered: {}", saved.getId());
        return mapAgentToResponse(saved);
    }

    public List<AgentResponse> getAvailableAgents() {
        return agentRepository.findByAvailableTrue()
                .stream()
                .map(this::mapAgentToResponse)
                .toList();
    }

    private AssignmentResponse mapToResponse(Assignment assignment) {
        return AssignmentResponse.builder()
                .id(assignment.getId())
                .orderId(assignment.getOrderId())
                .agentId(assignment.getAgentId())
                .status(assignment.getStatus())
                .failureReason(assignment.getFailureReason())
                .assignedAt(assignment.getAssignedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();
    }

    private AgentResponse mapAgentToResponse(DeliveryAgent agent) {
        return AgentResponse.builder()
                .id(agent.getId())
                .name(agent.getName())
                .phone(agent.getPhone())
                .available(agent.isAvailable())
                .currentLatitude(agent.getCurrentLatitude())
                .currentLongitude(agent.getCurrentLongitude())
                .build();
    }
}
