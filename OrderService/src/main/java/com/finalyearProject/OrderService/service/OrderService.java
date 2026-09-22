package com.finalyearProject.OrderService.service;

import com.finalyearProject.OrderService.dto.OrderRequest;
import com.finalyearProject.OrderService.dto.OrderResponse;
import com.finalyearProject.OrderService.enums.OrderStatus;
import com.finalyearProject.OrderService.kafka.OrderEventPublisher;
import com.finalyearProject.OrderService.model.Order;
import com.finalyearProject.OrderService.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .pickupAddress(request.getPickupAddress())
                .deliveryAddress(request.getDeliveryAddress())
                .packageDescription(request.getPackageDescription())
                .build();

        Order saved = orderRepository.save(order);
        orderEventPublisher.publishOrderCreated(saved.getId(), saved.getCustomerId());
        log.info("Order created with id: {}", saved.getId());
        return mapToResponse(saved);
    }

    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        return mapToResponse(order);
    }

    public List<OrderResponse> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);
        log.info("Order {} status updated to {}", orderId, newStatus);
        return mapToResponse(updated);
    }

    public OrderResponse cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setStatus(OrderStatus.CANCELLED);
        Order updated = orderRepository.save(order);
        orderEventPublisher.publishOrderCancelled(orderId);
        return mapToResponse(updated);
    }
    public void assignAgentToOrder(String orderId, String agentId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setAssignedAgentId(agentId);
        orderRepository.save(order);
        log.info("Order {} updated with agentId: {}", orderId, agentId);
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .pickupAddress(order.getPickupAddress())
                .deliveryAddress(order.getDeliveryAddress())
                .packageDescription(order.getPackageDescription())
                .status(order.getStatus())
                .assignedAgentId(order.getAssignedAgentId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
//
//    private final OrderRepository orderRepository;
//    private final OrderEventPublisher orderEventPublisher;
//
//    public OrderResponse createOrder(OrderRequest request) {
//        Order order = Order.builder()
//                .customerId(request.getCustomerId())
//                .pickupAddress(request.getPickupAddress())
//                .deliveryAddress(request.getDeliveryAddress())
//                .packageDescription(request.getPackageDescription())
//                .build();
//
//        Order saved = orderRepository.save(order);
//        orderEventPublisher.publishOrderCreated(saved.getId());
//        log.info("Order created with id: {}", saved.getId());
//        return mapToResponse(saved);
//    }
//
//    public OrderResponse getOrderById(String orderId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found lmao: " + orderId));
//        return mapToResponse(order);
//    }
//
//    public List<OrderResponse> getOrdersByCustomer(String customerId) {
//        return orderRepository.findByCustomerId(customerId)
//                .stream()
//                .map(this::mapToResponse)
//                .toList();
//    }
//
//    public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
//        order.setStatus(newStatus);
//        Order updated = orderRepository.save(order);
//        log.info("Order {} status updated to {}", orderId, newStatus);
//        return mapToResponse(updated);
//    }
//
//    public OrderResponse cancelOrder(String orderId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
//        order.setStatus(OrderStatus.CANCELLED);
//        Order updated = orderRepository.save(order);
//        orderEventPublisher.publishOrderCancelled(orderId);
//        return mapToResponse(updated);
//    }
//
//    private OrderResponse mapToResponse(Order order) {
//        return OrderResponse.builder()
//                .id(order.getId())
//                .customerId(order.getCustomerId())
//                .pickupAddress(order.getPickupAddress())
//                .deliveryAddress(order.getDeliveryAddress())
//                .packageDescription(order.getPackageDescription())
//                .status(order.getStatus())
//                .assignedAgentId(order.getAssignedAgentId())
//                .createdAt(order.getCreatedAt())
//                .updatedAt(order.getUpdatedAt())
//                .build();
//    }
}