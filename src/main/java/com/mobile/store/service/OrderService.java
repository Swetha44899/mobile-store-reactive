package com.mobile.store.service;

import com.mobile.store.domain.Order;
import com.mobile.store.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Flux<Order> getOrdersByMobileId(Long mobileId) {
        return orderRepository.findByMobileId(mobileId);
    }

    public Mono<Order> createOrder(Order order) {
        if (order == null) {
            return Mono.error(new IllegalArgumentException("Order payload cannot be null"));
        }
        if (order.getMobileId() == null) {
            return Mono.error(new IllegalArgumentException("mobileId is required"));
        }
        if (order.getCustomerName() == null || order.getCustomerName().isBlank()) {
            return Mono.error(new IllegalArgumentException("customerName is required"));
        }
        if (order.getQuantity() == null || order.getQuantity() <= 0) {
            return Mono.error(new IllegalArgumentException("quantity must be greater than zero"));
        }
        if (order.getTotalAmount() == null || order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new IllegalArgumentException("totalAmount must be greater than zero"));
        }
        if (order.getStatus() == null || order.getStatus().isBlank()) {
            order.setStatus("PLACED");
        }
        order.setCreatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
}
