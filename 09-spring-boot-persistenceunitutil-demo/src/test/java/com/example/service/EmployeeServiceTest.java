package com.example.service;

import com.example.controller.SpringBootPersistenceunitutilDemoApplication;
import com.example.controller.dto.PersistenceUtilResponse;
import com.example.controller.entity.Employee;
import com.example.controller.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SpringBootPersistenceunitutilDemoApplication.class)
@Transactional
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService service;

    @Test
    void shouldInspectEntity() {
        Employee employee = service.create(new Employee("Robert", "Hibernate Expert"));

        PersistenceUtilResponse response = service.inspect(employee.getId());

        assertNotNull(response);
        assertTrue(response.entityLoaded());
        assertTrue(response.isEmployee());
        assertEquals(Employee.class.getName(), response.entityClass());
        assertNotNull(response.version());
    }
}