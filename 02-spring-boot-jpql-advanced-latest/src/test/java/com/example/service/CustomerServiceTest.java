package com.example.service;

import com.example.controller.dto.CustomerDTO;
import com.example.controller.model.Customer;
import com.example.controller.repository.CustomerRepository;
import com.example.controller.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;
    private List<Customer> testCustomers;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setFirstName("John");
        testCustomer.setLastName("Smith");
        testCustomer.setEmail("john.smith@example.com");
        testCustomer.setCity("New York");
        testCustomer.setCountry("USA");
        testCustomer.setStatus("VIP");
        testCustomer.setTotalSpent(5000.0);

        Customer c2 = new Customer();
        c2.setId(2L);
        c2.setFirstName("Alice");
        c2.setLastName("Johnson");
        c2.setEmail("alice@example.com");
        c2.setCity("Los Angeles");
        c2.setCountry("USA");
        c2.setStatus("PREMIUM");
        c2.setTotalSpent(3000.0);

        testCustomers = List.of(testCustomer, c2);
    }

    // ============ CRUD TEST CASES ============

    @Test
    void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        Customer created = customerService.createCustomer(testCustomer);

        assertNotNull(created);
        assertEquals("John", created.getFirstName());
        assertEquals("john.smith@example.com", created.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        Optional<Customer> found = customerService.getCustomerById(1L);

        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomerByIdNotFound() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Customer> found = customerService.getCustomerById(999L);

        assertFalse(found.isPresent());
        verify(customerRepository, times(1)).findById(999L);
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(testCustomers);

        List<Customer> customers = customerService.getAllCustomers();

        assertNotNull(customers);
        assertEquals(2, customers.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCustomer() {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setFirstName("Jonathan");
        updatedCustomer.setStatus("PREMIUM");
        updatedCustomer.setTotalSpent(6000.0);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        Customer result = customerService.updateCustomer(1L, updatedCustomer);

        assertNotNull(result);
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomer() {
        doNothing().when(customerRepository).deleteById(1L);

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).deleteById(1L);
    }

    // ============ DTO CONVERSION TESTS ============

    @Test
    void testConvertToDTO() {
        CustomerDTO dto = customerService.convertToDTO(testCustomer);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("john.smith@example.com", dto.getEmail());
        assertEquals("New York", dto.getCity());
        assertEquals("VIP", dto.getStatus());
    }

    @Test
    void testConvertToDTOList() {
        List<CustomerDTO> dtos = customerService.convertToDTO(testCustomers);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("John", dtos.get(0).getFirstName());
        assertEquals("Alice", dtos.get(1).getFirstName());
    }

    // ============ UNION QUERY TESTS ============

    @Test
    void testGetVipAndPremiumCustomers() {
        when(customerRepository.findVipAndPremiumCustomers()).thenReturn(testCustomers);

        List<Customer> customers = customerService.getVipAndPremiumCustomers();

        assertNotNull(customers);
        assertEquals(2, customers.size());
        verify(customerRepository, times(1)).findVipAndPremiumCustomers();
    }

    @Test
    void testGetCustomersFromMultipleCities() {
        when(customerRepository.findCustomersFromMultipleCities("New York", "Los Angeles"))
            .thenReturn(testCustomers);

        List<Customer> customers = customerService.getCustomersFromMultipleCities("New York", "Los Angeles");

        assertNotNull(customers);
        assertEquals(2, customers.size());
        verify(customerRepository, times(1)).findCustomersFromMultipleCities("New York", "Los Angeles");
    }

    // ============ INTERSECT QUERY TESTS ============

    @Test
    void testGetActiveHighSpenders() {
        when(customerRepository.findActiveHighSpenders(2000.0)).thenReturn(List.of(testCustomer));

        List<Customer> customers = customerService.getActiveHighSpenders(2000.0);

        assertNotNull(customers);
        assertEquals(1, customers.size());
        verify(customerRepository, times(1)).findActiveHighSpenders(2000.0);
    }

    @Test
    void testGetCustomersByCountryAndStatus() {
        when(customerRepository.findCustomersByCountryAndStatus("USA", "VIP"))
            .thenReturn(List.of(testCustomer));

        List<Customer> customers = customerService.getCustomersByCountryAndStatus("USA", "VIP");

        assertNotNull(customers);
        assertEquals(1, customers.size());
        assertEquals("USA", customers.get(0).getCountry());
        verify(customerRepository, times(1)).findCustomersByCountryAndStatus("USA", "VIP");
    }

    // ============ EXCEPT QUERY TESTS ============

    @Test
    void testGetActiveCustomers() {
        when(customerRepository.findActiveCustomers()).thenReturn(testCustomers);

        List<Customer> customers = customerService.getActiveCustomers();

        assertNotNull(customers);
        assertEquals(2, customers.size());
        assertTrue(customers.stream().noneMatch(c -> "INACTIVE".equals(c.getStatus())));
        verify(customerRepository, times(1)).findActiveCustomers();
    }

    @Test
    void testGetCustomersNotFromCountry() {
        when(customerRepository.findCustomersNotFromCountry("France")).thenReturn(testCustomers);

        List<Customer> customers = customerService.getCustomersNotFromCountry("France");

        assertNotNull(customers);
        assertEquals(2, customers.size());
        verify(customerRepository, times(1)).findCustomersNotFromCountry("France");
    }

    // ============ CAST QUERY TESTS ============

    @Test
    void testGetCustomersBySpentAmount() {
        when(customerRepository.findCustomersBySpentAmount(2000)).thenReturn(List.of(testCustomer));

        List<Customer> customers = customerService.getCustomersBySpentAmount(2000);

        assertNotNull(customers);
        assertEquals(1, customers.size());
        verify(customerRepository, times(1)).findCustomersBySpentAmount(2000);
    }

    // ============ LEFT/RIGHT QUERY TESTS ============

    @Test
    void testGetCustomersByEmailPrefix() {
        when(customerRepository.findCustomersByEmailPrefix("joh")).thenReturn(List.of(testCustomer));

        List<Customer> customers = customerService.getCustomersByEmailPrefix("joh");

        assertNotNull(customers);
        assertEquals(1, customers.size());
        assertTrue(customers.get(0).getEmail().toLowerCase().startsWith("joh"));
        verify(customerRepository, times(1)).findCustomersByEmailPrefix("joh");
    }

    @Test
    void testGetCustomersByEmailDomain() {
        when(customerRepository.findCustomersByEmailDomain("example.com")).thenReturn(testCustomers);

        List<Customer> customers = customerService.getCustomersByEmailDomain("example.com");

        assertNotNull(customers);
        assertEquals(2, customers.size());
        verify(customerRepository, times(1)).findCustomersByEmailDomain("example.com");
    }

    // ============ REPLACE QUERY TESTS ============

    @Test
    void testGetCustomersWithReplacedFirstName() {
        when(customerRepository.findCustomersWithReplacedFirstName("John", "Jonathan"))
            .thenReturn(List.of(testCustomer));

        List<Customer> customers = customerService.getCustomersWithReplacedFirstName("John", "Jonathan");

        assertNotNull(customers);
        assertEquals(1, customers.size());
        verify(customerRepository, times(1)).findCustomersWithReplacedFirstName("John", "Jonathan");
    }

    // ============ COMPLEX QUERY TESTS ============

    @Test
    void testGetQualifiedVipAndPremium() {
        when(customerRepository.findQualifiedVipAndPremium()).thenReturn(testCustomers);

        List<Customer> customers = customerService.getQualifiedVipAndPremium();

        assertNotNull(customers);
        assertEquals(2, customers.size());
        verify(customerRepository, times(1)).findQualifiedVipAndPremium();
    }
}
