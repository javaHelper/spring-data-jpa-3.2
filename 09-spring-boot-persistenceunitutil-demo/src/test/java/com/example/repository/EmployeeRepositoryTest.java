package com.example.repository;

import com.example.controller.SpringBootPersistenceunitutilDemoApplication;
import com.example.controller.entity.Employee;
import com.example.controller.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = SpringBootPersistenceunitutilDemoApplication.class)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @Test
    void shouldFindAllEmployees() {
        List<Employee> employees = repository.findAll();
        assertEquals(3, employees.size());
    }

    @Test
    void shouldFindEmployeeById() {
        Employee employee = repository.findById(1L).orElseThrow();
        assertEquals("John", employee.getName());
    }

    @Test
    void shouldSaveEmployee() {
        Employee employee = new Employee("Robert", "Hibernate Expert");
        Employee saved = repository.save(employee);

        assertNotNull(saved.getId());
        assertEquals("Robert", saved.getName());
    }

    @Test
    void shouldUpdateEmployee() {
        Employee employee = repository.findById(1L).orElseThrow();
        employee.setName("John Updated");
        repository.save(employee);

        Employee updated = repository.findById(1L).orElseThrow();
        assertEquals("John Updated", updated.getName());
    }

    @Test
    void shouldDeleteEmployee() {
        repository.deleteById(1L);
        assertFalse(repository.findById(1L).isPresent());
    }
}