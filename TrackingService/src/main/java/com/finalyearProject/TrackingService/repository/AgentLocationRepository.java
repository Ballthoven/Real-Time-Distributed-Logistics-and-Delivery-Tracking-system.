package com.finalyearProject.TrackingService.repository;

import com.finalyearProject.TrackingService.model.AgentLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgentLocationRepository extends JpaRepository <AgentLocation, String> {
    List<AgentLocation> findByAgentIdOrderByTimestampDesc(String agentId);
    List<AgentLocation> findByOrderIdOrderByTimestampDesc(String orderId);
    Optional<AgentLocation> findTopByAgentIdOrderByTimestampDesc(String agentId);

}
