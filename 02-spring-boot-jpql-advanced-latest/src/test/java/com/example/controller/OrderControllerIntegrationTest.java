package com.example.controller;

import com.example.controller.model.Customer;
import com.example.controller.model.Order;
import com.example.controller.repository.CustomerRepository;
import com.example.controller.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();

        Customer c = new Customer();
        c.setFirstName("Test");
        c.setLastName("Customer");
        c.setEmail("test@example.com");
        c.setCity("Test City");
        c.setCountry("Test Country");
        c.setStatus("ACTIVE");
        c.setTotalSpent(0.0);
        testCustomer = customerRepository.save(c);

        Order o1 = new Order();
        o1.setCustomer(testCustomer);
        o1.setOrderDate(LocalDate.now());
        o1.setStatus("PENDING");
        o1.setTotalAmount(new BigDecimal("100"));
        o1.setPaymentStatus("UNPAID");

        Order o2 = new Order();
        o2.setCustomer(testCustomer);
        o2.setOrderDate(LocalDate.now());
        o2.setStatus("CONFIRMED");
        o2.setTotalAmount(new BigDecimal("500"));
        o2.setPaymentStatus("PAID");

        Order o3 = new Order();
        o3.setCustomer(testCustomer);
        o3.setOrderDate(LocalDate.now());
        o3.setStatus("DELIVERED");
        o3.setTotalAmount(new BigDecimal("1000"));
        o3.setPaymentStatus("PAID");

        Order o4 = new Order();
        o4.setCustomer(testCustomer);
        o4.setOrderDate(LocalDate.now());
        o4.setStatus("CANCELLED");
        o4.setTotalAmount(new BigDecimal("200"));
        o4.setPaymentStatus("REFUNDED");

        orderRepository.saveAll(java.util.List.of(o1, o2, o3, o4));
    }

    // ============ CRUD TESTS ============

    @Test
    void testCreateOrder() throws Exception {
        Order newOrder = new Order();
        newOrder.setCustomer(testCustomer);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setStatus("PENDING");
        newOrder.setTotalAmount(new BigDecimal("300"));
        newOrder.setPaymentStatus("UNPAID");

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.paymentStatus", is("UNPAID")));
    }

    @Test
    void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void testGetOrderById() throws Exception {
        Order order = orderRepository.findAll().get(0);
        Long orderId = order.getId();

        mockMvc.perform(get("/api/orders/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderId.intValue())));
    }

    @Test
    void testUpdateOrder() throws Exception {
        Order order = orderRepository.findAll().get(0);
        Long orderId = order.getId();

        order.setStatus("CONFIRMED");
        order.setPaymentStatus("PAID");

        mockMvc.perform(put("/api/orders/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CONFIRMED")))
                .andExpect(jsonPath("$.paymentStatus", is("PAID")));
    }

    @Test
    void testDeleteOrder() throws Exception {
        Order order = orderRepository.findAll().get(0);
        Long orderId = order.getId();

        mockMvc.perform(delete("/api/orders/" + orderId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isNotFound());
    }

    // ============ UNION TESTS ============

    @Test
    void testGetPendingOrCancelledOrders() throws Exception {
        mockMvc.perform(get("/api/orders/union/pending-cancelled")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    // ============ INTERSECT TESTS ============

    @Test
    void testGetConfirmedPaidOrders() throws Exception {
        mockMvc.perform(get("/api/orders/intersect/confirmed-paid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // ============ EXCEPT TESTS ============

    @Test
    void testGetUndeliveredOrders() throws Exception {
        mockMvc.perform(get("/api/orders/except/undelivered")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    // ============ CAST TESTS ============

    @Test
    void testGetOrdersByMinAmount() throws Exception {
        mockMvc.perform(get("/api/orders/cast/by-min-amount")
                        .param("minAmount", "500")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void testGetOrdersByMinAmountNoResults() throws Exception {
        mockMvc.perform(get("/api/orders/cast/by-min-amount")
                        .param("minAmount", "10000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
