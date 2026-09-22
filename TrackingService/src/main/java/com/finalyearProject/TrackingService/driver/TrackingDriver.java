package com.finalyearProject.TrackingService.driver;

import com.finalyearProject.TrackingService.dto.LocationUpdateRequest;
import com.finalyearProject.TrackingService.dto.LocationUpdateResponse;
import com.finalyearProject.TrackingService.service.TrackingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class TrackingDriver {

    private final TrackingService trackingService;

    @PostMapping("/location")
    public ResponseEntity<LocationUpdateResponse> updateLocation(
            @Valid @RequestBody LocationUpdateRequest request) {
        return ResponseEntity.ok(trackingService.updateLocation(request));
    }

    @GetMapping("/agent/{agentId}/history")
    public ResponseEntity<List<LocationUpdateResponse>> getHistory(
            @PathVariable String agentId) {
        return ResponseEntity.ok(trackingService.getLocationHistory(agentId));
    }

    @GetMapping("/agent/{agentId}/latest")
    public ResponseEntity<String> getLatest(@PathVariable String agentId) {
        return ResponseEntity.ok(trackingService.getLatestLocation(agentId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<LocationUpdateResponse>> getByOrder(
            @PathVariable String orderId) {
        return ResponseEntity.ok(trackingService.getLocationsByOrder(orderId));
    }


}
