package com.finalyearProject.OrderService.repository;

import com.finalyearProject.OrderService.enums.OrderStatus;
import com.finalyearProject.OrderService.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByCustomerId(String customerId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByAssignedAgentId(String agentId);
}
