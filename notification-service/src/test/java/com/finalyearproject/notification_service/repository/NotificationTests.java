package com.finalyearproject.notification_service.repository;
import com.finalyearproject.notification_service.model.Notification;
import com.finalyearproject.notification_service.model.NotificationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class NotificationTests {

    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 9, 28, 14, 30, 49);

    @Container
    @ServiceConnection

    @SuppressWarnings("rawtypes")

    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15");

    @Autowired
    private NotificationRepository notificationRepository;

    private Notification save(String orderId, String customerId, String message, NotificationStatus status) {
        return notificationRepository.saveAndFlush(Notification.builder()
                .orderId(orderId)
                .customerId(customerId)
                .message(message)
                .status(status)
                .createdAt(CREATED_AT)
                .build());
    }
    @Test
    public void NotificationRepositoryTest_FindById_returnFoundOrder(){
        NotificationStatus notificationStatus = NotificationStatus.values()[0];
        save("4g", "iw23", "Order is being processed", notificationStatus);
        save("4g", "iw23", "Order shipped", notificationStatus);
        save("9z", "iw23", "Other order", notificationStatus);
        List<Notification> result = notificationRepository.findByOrderId("4g");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Notification::getOrderId).containsOnly("4g");
        assertThat(result).extracting(Notification::getMessage)
                .containsExactlyInAnyOrder("Order is being processed", "Order shipped");
    }

    @Test
    void findByOrderId_returnsEmptyListWhenNoMatch() {
        save("4g", "iw23", "Order is being processed", NotificationStatus.values()[0]);

        assertThat(notificationRepository.findByOrderId("does-not-exist")).isEmpty();
    }

    @Test
    public void NotificationRepositoryTest_FindByOrderId_returnsFoundOrder(){
        NotificationStatus notificationStatus = NotificationStatus.values()[0];
        save("4g", "iw23", "I am number 1", notificationStatus);
        save("4k", "iw3", "I am number 2", notificationStatus);
        save("9z", "iwgd3", "Other order", notificationStatus);
        List<Notification> result = notificationRepository.findByCustomerId("iw23");
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Notification::getCustomerId).containsOnly("iw23");
        assertThat(result).extracting(Notification::getMessage)
                .containsExactlyInAnyOrder("iw2", "iwgd3");
    }

    @Test
    public void NotificationRepositoryTest_FindByCustomerId_returnsEmptyListWhenNoMatch() {
        save("4g", "iw23", "I am number 1", NotificationStatus.values()[0]);

        assertThat(notificationRepository.findByCustomerId("does-not-exist")).isEmpty();
    }


}



