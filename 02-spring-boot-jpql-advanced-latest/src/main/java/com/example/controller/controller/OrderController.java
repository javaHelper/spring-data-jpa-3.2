package com.example.controller.controller;

import com.example.controller.model.Order;
import com.example.controller.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
            .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    // UNION endpoint
    @GetMapping("/union/pending-cancelled")
    public ResponseEntity<List<Order>> getPendingOrCancelledOrders() {
        return new ResponseEntity<>(orderService.getPendingOrCancelledOrders(), HttpStatus.OK);
    }

    // INTERSECT endpoint
    @GetMapping("/intersect/confirmed-paid")
    public ResponseEntity<List<Order>> getConfirmedPaidOrders() {
        return new ResponseEntity<>(orderService.getConfirmedPaidOrders(), HttpStatus.OK);
    }

    // EXCEPT endpoint
    @GetMapping("/except/undelivered")
    public ResponseEntity<List<Order>> getUndeliveredOrders() {
        return new ResponseEntity<>(orderService.getUndeliveredOrders(), HttpStatus.OK);
    }

    // CAST endpoint
    @GetMapping("/cast/by-min-amount")
    public ResponseEntity<List<Order>> getOrdersByMinAmount(@RequestParam Integer minAmount) {
        return new ResponseEntity<>(orderService.getOrdersByMinAmount(minAmount), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        return new ResponseEntity<>(orderService.updateOrder(id, orderDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}