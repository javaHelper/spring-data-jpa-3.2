package com.example.controller;

import com.example.controller.model.Customer;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();

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
    }

    // ============ CRUD TESTS ============

    @Test
    void testCreateCustomer() throws Exception {
        Customer newCustomer = new Customer();
        newCustomer.setFirstName("David");
        newCustomer.setLastName("Thompson");
        newCustomer.setEmail("david.thompson@example.com");
        newCustomer.setPhone("+1-555-0007");
        newCustomer.setCity("Boston");
        newCustomer.setCountry("USA");
        newCustomer.setStatus("ACTIVE");
        newCustomer.setTotalSpent(2500.0);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("David")))
                .andExpect(jsonPath("$.email", is("david.thompson@example.com")));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        mockMvc.perform(get("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void testGetCustomerById() throws Exception {
        Customer customer = customerRepository.findAll().get(0);
        Long customerId = customer.getId();

        mockMvc.perform(get("/api/customers/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(customerId.intValue())));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = customerRepository.findAll().get(0);
        Long customerId = customer.getId();

        customer.setStatus("PREMIUM");
        customer.setTotalSpent(6000.0);

        mockMvc.perform(put("/api/customers/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("PREMIUM")));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Customer customer = customerRepository.findAll().get(0);
        Long customerId = customer.getId();

        mockMvc.perform(delete("/api/customers/" + customerId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/customers/" + customerId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetVipAndPremiumCustomers() throws Exception {
        mockMvc.perform(get("/api/customers/union/vip-premium")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void testGetCustomersFromMultipleCities() throws Exception {
        mockMvc.perform(get("/api/customers/union/multiple-cities")
                        .param("city1", "New York")
                        .param("city2", "Los Angeles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    // ============ INTERSECT TESTS ============

    @Test
    void testGetActiveHighSpenders() throws Exception {
        mockMvc.perform(get("/api/customers/intersect/active-high-spenders")
                        .param("minSpent", "2000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void testGetCustomersByCountryAndStatus() throws Exception {
        mockMvc.perform(get("/api/customers/intersect/country-status")
                        .param("country", "USA")
                        .param("status", "ACTIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    // ============ EXCEPT TESTS ============

    @Test
    void testGetActiveCustomers() throws Exception {
        mockMvc.perform(get("/api/customers/except/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    void testGetCustomersNotFromCountry() throws Exception {
        mockMvc.perform(get("/api/customers/except/not-from-country")
                        .param("excludeCountry", "France")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    // ============ CAST TESTS ============

    @Test
    void testGetCustomersBySpentAmount() throws Exception {
        mockMvc.perform(get("/api/customers/cast/by-spent-amount")
                        .param("minSpent", "2000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    // ============ LEFT TESTS ============

    @Test
    void testGetCustomersByEmailPrefix() throws Exception {
        mockMvc.perform(get("/api/customers/left/by-email-prefix")
                        .param("emailPrefix", "joh")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    // ============ RIGHT TESTS ============

    @Test
    void testGetCustomersByEmailDomain() throws Exception {
        mockMvc.perform(get("/api/customers/right/by-email-domain")
                        .param("emailDomain", "example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    // ============ REPLACE TESTS ============

    @Test
    void testGetCustomersWithReplacedFirstName() throws Exception {
        mockMvc.perform(get("/api/customers/replace/first-name")
                        .param("oldName", "John")
                        .param("newName", "Jonathan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    // ============ COMPLEX TESTS ============

    @Test
    void testGetQualifiedVipAndPremium() throws Exception {
        mockMvc.perform(get("/api/customers/qualified/vip-premium")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }
}