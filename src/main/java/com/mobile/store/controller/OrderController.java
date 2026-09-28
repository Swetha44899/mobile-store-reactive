package com.mobile.store.controller;

import com.mobile.store.domain.Order;
import com.mobile.store.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping(value = "/orders/mobile/{mobileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Order> getOrdersByMobileId(@PathVariable Long mobileId) {
        return orderService.getOrdersByMobileId(mobileId);
    }

    @PostMapping(value = "/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Order> createOrder(@Valid @RequestBody Order order) {
        return orderService.createOrder(order);
    }
}
