package com.FinalYearProject.AssignmentService.repository;

import com.FinalYearProject.AssignmentService.model.DeliveryAgent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, String> {
    List<DeliveryAgent> findByAvailableTrue();
    Optional<DeliveryAgent> findByPhone(String phone);
}
