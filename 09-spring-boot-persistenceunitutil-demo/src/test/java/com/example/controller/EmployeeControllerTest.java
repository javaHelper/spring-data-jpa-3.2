package com.example.controller;

import com.example.controller.controller.EmployeeController;
import com.example.controller.dto.PersistenceUtilResponse;
import com.example.controller.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    EmployeeService service;

    @Test
    void inspect() throws Exception {

        PersistenceUtilResponse dto = new PersistenceUtilResponse(
                0L,
                true,
                false,
                true,
                "com.example.entity.Employee"
        );

        when(service.inspect(1L)).thenReturn(dto);

        mvc.perform(get("/employees/1/inspect"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(0))
                .andExpect(jsonPath("$.entityLoaded").value(true))
                .andExpect(jsonPath("$.profileLoaded").value(false))
                .andExpect(jsonPath("$.isEmployee").value(true));
    }

}