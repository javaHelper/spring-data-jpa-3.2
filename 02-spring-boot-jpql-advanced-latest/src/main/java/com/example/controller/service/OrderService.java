package com.example.controller.service;

import com.example.controller.model.Order;
import com.example.controller.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // UNION example
    public List<Order> getPendingOrCancelledOrders() {
        return orderRepository.findPendingOrCancelledOrders();
    }

    // INTERSECT example
    public List<Order> getConfirmedPaidOrders() {
        return orderRepository.findConfirmedPaidOrders();
    }

    // EXCEPT example
    public List<Order> getUndeliveredOrders() {
        return orderRepository.findUndeliveredOrders();
    }

    // CAST example
    public List<Order> getOrdersByMinAmount(Integer minAmount) {
        return orderRepository.findOrdersByMinAmount(minAmount);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(orderDetails.getStatus());
                    order.setPaymentStatus(orderDetails.getPaymentStatus());
                    order.setTotalAmount(orderDetails.getTotalAmount());
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
