package com.example.controller;

import com.example.entity.Employee;
import com.example.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return all employees")
    void shouldReturnEmployees() throws Exception {

        Employee e1 = new Employee(
                1L,
                "John",
                "IT",
                60000.0,
                0
        );

        Employee e2 = new Employee(
                2L,
                "Alice",
                "HR",
                50000.0,
                0
        );

        when(employeeService.loadEmployees())
                .thenReturn(List.of(e1, e2));

        mockMvc.perform(get("/employees/native"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].department").value("IT"))
                .andExpect(jsonPath("$[0].salary").value(60000.0))
                .andExpect(jsonPath("$[0].version").value(0))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Alice"))
                .andExpect(jsonPath("$[1].department").value("HR"))
                .andExpect(jsonPath("$[1].salary").value(50000.0))
                .andExpect(jsonPath("$[1].version").value(0));
    }

    @Test
    @DisplayName("Should return empty list")
    void shouldReturnEmptyList() throws Exception {

        when(employeeService.loadEmployees())
                .thenReturn(List.of());

        mockMvc.perform(get("/employees/native"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("Should return Internal Server Error")
    void shouldHandleException() throws Exception {

        when(employeeService.loadEmployees())
                .thenThrow(new RuntimeException("Database Error"));

        mockMvc.perform(get("/employees/native"))
                .andExpect(status().is5xxServerError());
    }
}