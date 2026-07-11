package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturnEntities() throws Exception {
        mockMvc.perform(get("/employees/entities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    void shouldReturnSummaryDTO() throws Exception {
        mockMvc.perform(get("/employees/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    void shouldReturnDepartmentStatistics() throws Exception {
        mockMvc.perform(get("/employees/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].department").exists())
                .andExpect(jsonPath("$[0].count").exists());
    }
}