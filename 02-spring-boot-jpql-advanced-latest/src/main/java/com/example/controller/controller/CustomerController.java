package com.example.controller.controller;

import com.example.controller.dto.CustomerDTO;
import com.example.controller.model.Customer;
import com.example.controller.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.createCustomer(customer), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(customer -> new ResponseEntity<>(customerService.convertToDTO(customer), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customerService.convertToDTO(customers), HttpStatus.OK);
    }

    // UNION endpoints
    @GetMapping("/union/vip-premium")
    public ResponseEntity<List<CustomerDTO>> getVipAndPremiumCustomers() {
        List<Customer> customers = customerService.getVipAndPremiumCustomers();
        return new ResponseEntity<>(customerService.convertToDTO(customers), HttpStatus.OK);
    }

    @GetMapping("/union/multiple-cities")
    public ResponseEntity<List<CustomerDTO>> getCustomersFromMultipleCities(
            @RequestParam String city1,
            @RequestParam String city2) {
        List<Customer> customers = customerService.getCustomersFromMultipleCities(city1, city2);
        return new ResponseEntity<>(customerService.convertToDTO(customers), HttpStatus.OK);
    }

    // INTERSECT endpoints
    @GetMapping("/intersect/active-high-spenders")
    public ResponseEntity<List<CustomerDTO>> getActiveHighSpenders(@RequestParam Double minSpent) {
        List<Customer> customers = customerService.getActiveHighSpenders(minSpent);
        return new ResponseEntity<>(customerService.convertToDTO(customers), HttpStatus.OK);
    }

    @GetMapping("/intersect/country-status")
    public ResponseEntity<List<CustomerDTO>> getCustomersByCountryAndStatus(
            @RequestParam String country,
            @RequestParam String status) {
        List<Customer> customers = customerService.getCustomersByCountryAndStatus(country, status);
        return new ResponseEntity<>(customerService.convertToDTO(customers), HttpStatus.OK);
    }

    // EXCEPT endpoints
    @GetMapping("/except/active")
    public ResponseEntity<List<CustomerDTO>> getActiveCustomers() {
        List<Customer> customers = customerService.getActiveCustomers();
        return new ResponseEntity<>(customerService.convertToDTO(customers), HttpStatus.OK);
    }

    @GetMapping("/except/not-from-country")
    public ResponseEntity<List<CustomerDTO>> getCustomersNotFromCountry(@RequestParam String excludeCountry) {
        List<Customer> customers = customerService.getCustomersNotFromCountry(excludeCountry);
        return new ResponseEntity<>(customerService.convertToDTO(customers), HttpStatus.OK);
    }

    // CAST endpoint
    @GetMapping("/cast/by-spent-amount")
    public ResponseEntity<List<CustomerDTO>> getCustomersBySpentAmount(@RequestParam Integer minSpent) {
        List<Customer> customers = customerService.getCustomersBySpentAmount(minSpent);
        return new ResponseEntity<>(customerService.convertToDTO(customers), HttpStatus.OK);
    }

    // LEFT endpoint
    @GetMapping("/left/by-email-prefix")
    public ResponseEntity<List<CustomerDTO>> getCustomersByEmailPrefix(@RequestParam String emailPrefix) {
        List<Customer> customers = customerService.getCustomersByEmailPrefix(emailPrefix);
        return new ResponseEntity<>(customerService.convertToDTO(customers), HttpStatus.OK);
    }

    // RIGHT endpoint
    @GetMapping("/right/by-email-domain")
    public ResponseEntity<List<CustomerDTO>> getCustomersByEmailDomain(@RequestParam String emailDomain) {
        List<Customer> customers = customerService.getCustomersByEmailDomain(emailDomain);
        return new ResponseEntity<>(customerService.convertToDTO(customers), HttpStatus.OK);
    }

    // REPLACE endpoint
    @GetMapping("/replace/first-name")
    public ResponseEntity<List<CustomerDTO>> getCustomersWithReplacedFirstName(
            @RequestParam String oldName,
            @RequestParam String newName) {
        List<Customer> customers = customerService.getCustomersWithReplacedFirstName(oldName, newName);
        return new ResponseEntity<>(customerService.convertToDTO(customers), HttpStatus.OK);
    }

    // Complex query
    @GetMapping("/qualified/vip-premium")
    public ResponseEntity<List<CustomerDTO>> getQualifiedVipAndPremium() {
        List<Customer> customers = customerService.getQualifiedVipAndPremium();
        return new ResponseEntity<>(customerService.convertToDTO(customers), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        return new ResponseEntity<>(customerService.updateCustomer(id, customerDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
