package com.example.service;

import com.example.controller.model.Customer;
import com.example.controller.model.Order;
import com.example.controller.repository.OrderRepository;
import com.example.controller.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;
    private Customer testCustomer;
    private List<Order> testOrders;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setFirstName("Test");
        testCustomer.setLastName("Customer");

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setCustomer(testCustomer);
        testOrder.setOrderDate(LocalDate.now());
        testOrder.setStatus("CONFIRMED");
        testOrder.setTotalAmount(new BigDecimal("500.00"));
        testOrder.setPaymentStatus("PAID");

        Order o2 = new Order();
        o2.setId(2L);
        o2.setCustomer(testCustomer);
        o2.setOrderDate(LocalDate.now());
        o2.setStatus("PENDING");
        o2.setTotalAmount(new BigDecimal("100.00"));
        o2.setPaymentStatus("UNPAID");

        testOrders = List.of(testOrder, o2);
    }

    // ============ CRUD TEST CASES ============

    @Test
    void testCreateOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order created = orderService.createOrder(testOrder);

        assertNotNull(created);
        assertEquals("CONFIRMED", created.getStatus());
        assertEquals("PAID", created.getPaymentStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Optional<Order> found = orderService.getOrderById(1L);

        assertTrue(found.isPresent());
        assertEquals("CONFIRMED", found.get().getStatus());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Order> found = orderService.getOrderById(999L);

        assertFalse(found.isPresent());
        verify(orderRepository, times(1)).findById(999L);
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(testOrders);

        List<Order> orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testUpdateOrder() {
        Order updatedOrder = new Order();
        updatedOrder.setStatus("SHIPPED");
        updatedOrder.setPaymentStatus("PAID");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.updateOrder(1L, updatedOrder);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }

    // ============ UNION QUERY TESTS ============

    @Test
    void testGetPendingOrCancelledOrders() {
        when(orderRepository.findPendingOrCancelledOrders()).thenReturn(testOrders);

        List<Order> orders = orderService.getPendingOrCancelledOrders();

        assertNotNull(orders);
        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findPendingOrCancelledOrders();
    }

    // ============ INTERSECT QUERY TESTS ============

    @Test
    void testGetConfirmedPaidOrders() {
        when(orderRepository.findConfirmedPaidOrders()).thenReturn(List.of(testOrder));

        List<Order> orders = orderService.getConfirmedPaidOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals("CONFIRMED", orders.get(0).getStatus());
        assertEquals("PAID", orders.get(0).getPaymentStatus());
        verify(orderRepository, times(1)).findConfirmedPaidOrders();
    }

    // ============ EXCEPT QUERY TESTS ============

    @Test
    void testGetUndeliveredOrders() {
        when(orderRepository.findUndeliveredOrders()).thenReturn(testOrders);

        List<Order> orders = orderService.getUndeliveredOrders();

        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertTrue(orders.stream().noneMatch(o -> "DELIVERED".equals(o.getStatus())));
        verify(orderRepository, times(1)).findUndeliveredOrders();
    }

    // ============ CAST QUERY TESTS ============

    @Test
    void testGetOrdersByMinAmount() {
        when(orderRepository.findOrdersByMinAmount(100)).thenReturn(testOrders);

        List<Order> orders = orderService.getOrdersByMinAmount(100);

        assertNotNull(orders);
        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findOrdersByMinAmount(100);
    }

    @Test
    void testGetOrdersByMinAmountNoResults() {
        when(orderRepository.findOrdersByMinAmount(10000)).thenReturn(List.of());

        List<Order> orders = orderService.getOrdersByMinAmount(10000);

        assertNotNull(orders);
        assertEquals(0, orders.size());
        verify(orderRepository, times(1)).findOrdersByMinAmount(10000);
    }
}