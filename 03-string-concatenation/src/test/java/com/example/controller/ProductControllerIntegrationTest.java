package com.example.controller;

import com.example.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldReturnProductInfo() throws Exception {
        // Given a product exists (inserted via DataLoader or manually)
        // We'll use the DataLoader from the app, so we assume product 1 exists.

        mockMvc.perform(get("/products/1/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concatenated").value("Laptop - High-performance portable computer"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.version").value(0));
    }

    @Test
    void shouldSearchByFullText() throws Exception {
        mockMvc.perform(get("/products/search")
                        .param("name", "Mouse")
                        .param("description", "Wireless ergonomic mouse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mouse"));
    }

    @Test
    void shouldCheckByIdAndVersion() throws Exception {
        mockMvc.perform(get("/products/check")
                        .param("id", "1")
                        .param("version", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        // Wrong version should return null (entity not found) – controller returns null, which serializes to empty JSON?
        // Depending on your controller, you may want to handle not found; but we just test the query.
        mockMvc.perform(get("/products/check")
                        .param("id", "1")
                        .param("version", "99"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));  // because controller returns null -> empty response
    }
}