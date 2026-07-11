package com.example.controller.initializer;

import com.example.controller.model.Product;
import com.example.controller.model.Customer;
import com.example.controller.model.Order;
import com.example.controller.repository.ProductRepository;
import com.example.controller.repository.CustomerRepository;
import com.example.controller.repository.OrderRepository;
import com.example.controller.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeProducts();
        initializeCustomers();
        initializeOrders();
    }

    private void initializeProducts() {
        // ACTIVE products
        Product p1 = new Product();
        p1.setName("Laptop Professional");
        p1.setDescription("High-performance business laptop");
        p1.setCode("LAP001");
        p1.setCategory("Electronics");
        p1.setPrice(new BigDecimal("1200.00"));
        p1.setStock(50);
        p1.setStatus("ACTIVE");
        p1.setSupplier("TechCorp");

        Product p2 = new Product();
        p2.setName("Office Chair");
        p2.setDescription("Ergonomic office chair for comfort");
        p2.setCode("CHR002");
        p2.setCategory("Furniture");
        p2.setPrice(new BigDecimal("350.00"));
        p2.setStock(120);
        p2.setStatus("ACTIVE");
        p2.setSupplier("FurniturePlus");

        Product p3 = new Product();
        p3.setName("Desk Monitor");
        p3.setDescription("27 inch 4K monitor for work");
        p3.setCode("MON003");
        p3.setCategory("Electronics");
        p3.setPrice(new BigDecimal("400.00"));
        p3.setStock(0);
        p3.setStatus("ACTIVE");
        p3.setSupplier("TechCorp");

        // INACTIVE products
        Product p4 = new Product();
        p4.setName("Old Printer");
        p4.setDescription("Deprecated printer model");
        p4.setCode("PRI004");
        p4.setCategory("Electronics");
        p4.setPrice(new BigDecimal("250.00"));
        p4.setStock(5);
        p4.setStatus("INACTIVE");
        p4.setSupplier("OfficePlus");

        // DISCONTINUED products
        Product p5 = new Product();
        p5.setName("CRT Monitor");
        p5.setDescription("Old CRT monitor discontinued");
        p5.setCode("CRT005");
        p5.setCategory("Electronics");
        p5.setPrice(new BigDecimal("50.00"));
        p5.setStock(0);
        p5.setStatus("DISCONTINUED");
        p5.setSupplier("TechCorp");

        Product p6 = new Product();
        p6.setName("Expensive Keyboard");
        p6.setDescription("Premium mechanical keyboard");
        p6.setCode("KEY006");
        p6.setCategory("Accessories");
        p6.setPrice(new BigDecimal("1500.00"));
        p6.setStock(10);
        p6.setStatus("ACTIVE");
        p6.setSupplier("KeyMaster");

        productRepository.saveAll(java.util.List.of(p1, p2, p3, p4, p5, p6));
        System.out.println("✅ Products initialized successfully!");
    }

    private void initializeCustomers() {
        Customer c1 = new Customer();
        c1.setFirstName("John");
        c1.setLastName("Smith");
        c1.setEmail("john.smith@example.com");
        c1.setPhone("+1-555-0001");
        c1.setAddress("123 Main St");
        c1.setCity("New York");
        c1.setCountry("USA");
        c1.setStatus("VIP");
        c1.setTotalSpent(5000.0);

        Customer c2 = new Customer();
        c2.setFirstName("Alice");
        c2.setLastName("Johnson");
        c2.setEmail("alice.johnson@example.com");
        c2.setPhone("+1-555-0002");
        c2.setAddress("456 Oak Ave");
        c2.setCity("Los Angeles");
        c2.setCountry("USA");
        c2.setStatus("PREMIUM");
        c2.setTotalSpent(3000.0);

        Customer c3 = new Customer();
        c3.setFirstName("Robert");
        c3.setLastName("Brown");
        c3.setEmail("robert.brown@example.com");
        c3.setPhone("+1-555-0003");
        c3.setAddress("789 Pine Rd");
        c3.setCity("Chicago");
        c3.setCountry("USA");
        c3.setStatus("ACTIVE");
        c3.setTotalSpent(1500.0);

        Customer c4 = new Customer();
        c4.setFirstName("Emma");
        c4.setLastName("Davis");
        c4.setEmail("emma.davis@example.com");
        c4.setPhone("+1-555-0004");
        c4.setAddress("321 Elm St");
        c4.setCity("London");
        c4.setCountry("UK");
        c4.setStatus("ACTIVE");
        c4.setTotalSpent(2000.0);

        Customer c5 = new Customer();
        c5.setFirstName("Michael");
        c5.setLastName("Wilson");
        c5.setEmail("michael.wilson@example.com");
        c5.setPhone("+1-555-0005");
        c5.setAddress("654 Maple Dr");
        c5.setCity("Paris");
        c5.setCountry("France");
        c5.setStatus("INACTIVE");
        c5.setTotalSpent(500.0);

        Customer c6 = new Customer();
        c6.setFirstName("Sarah");
        c6.setLastName("Martinez");
        c6.setEmail("sarah.martinez@example.com");
        c6.setPhone("+1-555-0006");
        c6.setCity("Toronto");
        c6.setCountry("Canada");
        c6.setStatus("PREMIUM");
        c6.setTotalSpent(4000.0);

        customerRepository.saveAll(java.util.List.of(c1, c2, c3, c4, c5, c6));
        System.out.println("✅ Customers initialized successfully!");
    }

    private void initializeOrders() {
        Customer c1 = customerRepository.findAll().get(0);

        Order o1 = new Order();
        o1.setCustomer(c1);
        o1.setOrderDate(LocalDate.now());
        o1.setStatus("CONFIRMED");
        o1.setTotalAmount(new BigDecimal("1600.00"));
        o1.setPaymentStatus("PAID");

        Order o2 = new Order();
        o2.setCustomer(c1);
        o2.setOrderDate(LocalDate.now().minusDays(5));
        o2.setStatus("PENDING");
        o2.setTotalAmount(new BigDecimal("350.00"));
        o2.setPaymentStatus("UNPAID");

        Order o3 = new Order();
        o3.setCustomer(c1);
        o3.setOrderDate(LocalDate.now().minusDays(10));
        o3.setStatus("DELIVERED");
        o3.setTotalAmount(new BigDecimal("2000.00"));
        o3.setPaymentStatus("PAID");

        Order o4 = new Order();
        o4.setCustomer(c1);
        o4.setOrderDate(LocalDate.now().minusDays(15));
        o4.setStatus("CANCELLED");
        o4.setTotalAmount(new BigDecimal("500.00"));
        o4.setPaymentStatus("REFUNDED");

        orderRepository.saveAll(java.util.List.of(o1, o2, o3, o4));
        System.out.println("✅ Orders initialized successfully!");
    }
}
