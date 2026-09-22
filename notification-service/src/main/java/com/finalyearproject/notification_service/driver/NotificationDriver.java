package com.finalyearproject.notification_service.driver;

import com.finalyearproject.notification_service.dto.NotificationRequest;
import com.finalyearproject.notification_service.dto.NotificationResponse;
import com.finalyearproject.notification_service.repository.NotificationRepository;
import com.finalyearproject.notification_service.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationDriver {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@Valid @RequestBody NotificationRequest request) {
        notificationService.sendNotification(
                request.getOrderId(),
                request.getCustomerId(),
                "Your notification has been sent"
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<NotificationResponse>> getByOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(notificationService.getNotificationsByOrder(orderId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<NotificationResponse>> getByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(notificationService.getNotificationsByCustomer(customerId));
    }
}
