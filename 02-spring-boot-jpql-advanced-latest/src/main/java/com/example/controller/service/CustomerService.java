package com.example.controller.service;

import com.example.controller.dto.CustomerDTO;
import com.example.controller.model.Customer;
import com.example.controller.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // UNION examples - Now return entities
    public List<Customer> getVipAndPremiumCustomers() {
        return customerRepository.findVipAndPremiumCustomers();
    }

    public List<Customer> getCustomersFromMultipleCities(String city1, String city2) {
        return customerRepository.findCustomersFromMultipleCities(city1, city2);
    }

    // Convert to DTO for API response
    public List<CustomerDTO> convertToDTO(List<Customer> customers) {
        return customers.stream()
                .map(c -> new CustomerDTO(c.getId(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getCity(), c.getStatus()))
                .collect(Collectors.toList());
    }

    public CustomerDTO convertToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getCity(),
                customer.getStatus()
        );
    }

    // INTERSECT examples
    public List<Customer> getActiveHighSpenders(Double minSpent) {
        return customerRepository.findActiveHighSpenders(minSpent);
    }

    public List<Customer> getCustomersByCountryAndStatus(String country, String status) {
        return customerRepository.findCustomersByCountryAndStatus(country, status);
    }

    // EXCEPT examples
    public List<Customer> getActiveCustomers() {
        return customerRepository.findActiveCustomers();
    }

    public List<Customer> getCustomersNotFromCountry(String excludeCountry) {
        return customerRepository.findCustomersNotFromCountry(excludeCountry);
    }

    // CAST example
    public List<Customer> getCustomersBySpentAmount(Integer minSpent) {
        return customerRepository.findCustomersBySpentAmount(minSpent);
    }

    // LEFT example
    public List<Customer> getCustomersByEmailPrefix(String emailPrefix) {
        return customerRepository.findCustomersByEmailPrefix(emailPrefix);
    }

    // RIGHT example
    public List<Customer> getCustomersByEmailDomain(String emailDomain) {
        return customerRepository.findCustomersByEmailDomain(emailDomain);
    }

    // REPLACE example
    public List<Customer> getCustomersWithReplacedFirstName(String oldName, String newName) {
        return customerRepository.findCustomersWithReplacedFirstName(oldName, newName);
    }

    // Complex query
    public List<Customer> getQualifiedVipAndPremium() {
        return customerRepository.findQualifiedVipAndPremium();
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFirstName(customerDetails.getFirstName());
                    customer.setLastName(customerDetails.getLastName());
                    customer.setEmail(customerDetails.getEmail());
                    customer.setCity(customerDetails.getCity());
                    customer.setStatus(customerDetails.getStatus());
                    customer.setTotalSpent(customerDetails.getTotalSpent());
                    return customerRepository.save(customer);
                })
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}