package com.example.service;

import com.example.controller.SpringBootPersistenceunitutilDemoApplication;
import com.example.controller.entity.Employee;
import com.example.controller.repository.EmployeeRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = SpringBootPersistenceunitutilDemoApplication.class)
class PersistenceUnitUtilTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository repository;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    void shouldVerifyPersistenceUnitUtilMethods() {
        Employee employee = repository.findById(1L).orElseThrow();

        PersistenceUnitUtil util = emf.getPersistenceUnitUtil();
        assertTrue(util.isLoaded(employee));
        assertTrue(util.isLoaded(employee, "profile"));
        assertEquals(Employee.class, util.getClass(employee));

        assertTrue(util.isInstance(employee, Employee.class));

        assertEquals(0L, util.getVersion(employee));
        util.load(employee);
        util.load(employee, "profile");
        assertTrue(util.isLoaded(employee));
    }
}