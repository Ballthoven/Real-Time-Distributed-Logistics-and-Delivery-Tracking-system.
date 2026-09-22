package com.finalyearproject.notification_service.repository;

import com.finalyearproject.notification_service.model.Notification;
import com.finalyearproject.notification_service.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByOrderId(String orderId);
    List<Notification> findByCustomerId(String customerId);
    List<Notification> findByStatus(NotificationStatus status);
}
