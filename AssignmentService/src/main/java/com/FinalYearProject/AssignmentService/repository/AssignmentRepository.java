package com.FinalYearProject.AssignmentService.repository;

import com.FinalYearProject.AssignmentService.model.Assignment;
import com.FinalYearProject.AssignmentService.model.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, String> {
    Optional<Assignment> findTopByOrderIdOrderByAssignedAtDesc(String orderId);
    List<Assignment> findByAgentId(String agentId);
    List<Assignment> findByStatus(AssignmentStatus status);
}
