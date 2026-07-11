package com.example.service;

import com.example.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
class EmployeeServiceTest {

    @Autowired
    EmployeeService service;

    @Test
    void shouldReturnEmployeeUsingQuery(){
        Employee employee = service.findUsingQuery(1L);
        assertNotNull(employee);
        assertEquals("John", employee.getName());
    }

    @Test
    void shouldReturnNullWhenNotFound(){
        Employee employee = service.findUsingQuery(100L);
        assertNull(employee);
    }

    @Test
    void shouldReturnEmployeeUsingTypedQuery(){
        Employee employee = service.findUsingTypedQuery("john@test.com");
        assertNotNull(employee);
    }

    @Test
    void shouldReturnNullUsingTypedQuery(){
        Employee employee = service.findUsingTypedQuery("abc@test.com");
        assertNull(employee);
    }

    @Test
    void shouldReturnEmployeeUsingStoredProcedure(){
        Employee employee = service.findUsingStoredProcedure("john@test.com");
        assertNotNull(employee);
    }

    @Test
    void shouldReturnNullUsingStoredProcedure(){
        Employee employee = service.findUsingStoredProcedure("xyz@test.com");
        assertNull(employee);
    }
}