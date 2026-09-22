package com.finalyearProject.TrackingService.service;


import com.finalyearProject.TrackingService.dto.LocationUpdateRequest;
import com.finalyearProject.TrackingService.dto.LocationUpdateResponse;
import com.finalyearProject.TrackingService.model.AgentLocation;
import com.finalyearProject.TrackingService.repository.AgentLocationRepository;
import com.finalyearProject.TrackingService.websocket.LocationWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TrackingService {
    private final AgentLocationRepository locationRepository;
    private final RedisLocationService redisLocationService;

    @Autowired(required = false)
    private LocationWebSocketHandler webSocketHandler;

    public TrackingService(AgentLocationRepository locationRepository,
                           RedisLocationService redisLocationService) {
        this.locationRepository = locationRepository;
        this.redisLocationService = redisLocationService;
    }

    public LocationUpdateResponse updateLocation(LocationUpdateRequest request) {
        AgentLocation location = AgentLocation.builder()
                .agentId(request.getAgentId())
                .orderId(request.getOrderId())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        AgentLocation saved = locationRepository.save(location);
        redisLocationService.saveLocation(request.getAgentId(),
                request.getLatitude(), request.getLongitude());

        if (webSocketHandler != null) {
            String message = String.format(
                    "{\"agentId\":\"%s\",\"orderId\":\"%s\",\"latitude\":%s,\"longitude\":%s}",
                    saved.getAgentId(), saved.getOrderId(),
                    saved.getLatitude(), saved.getLongitude()
            );
            webSocketHandler.broadcastLocation(message);
        }

        log.info("Location updated for agent: {}", request.getAgentId());
        return mapToResponse(saved);
    }

    public List<LocationUpdateResponse> getLocationHistory(String agentId) {
        return locationRepository.findByAgentIdOrderByTimestampDesc(agentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<LocationUpdateResponse> getLocationsByOrder(String orderId) {
        return locationRepository.findByOrderIdOrderByTimestampDesc(orderId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public String getLatestLocation(String agentId) {
        String cached = redisLocationService.getLocation(agentId);
        if (cached != null) {
            return cached;
        }
        return locationRepository.findTopByAgentIdOrderByTimestampDesc(agentId)
                .map(l -> l.getLatitude() + "," + l.getLongitude())
                .orElse("Location not found");
    }

    private LocationUpdateResponse mapToResponse(AgentLocation location) {
        return LocationUpdateResponse.builder()
                .id(location.getId())
                .agentId(location.getAgentId())
                .orderId(location.getOrderId())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .locationStatus(location.getStatus())
                .timestamp(location.getTimestamp())
                .build();
    }
}
