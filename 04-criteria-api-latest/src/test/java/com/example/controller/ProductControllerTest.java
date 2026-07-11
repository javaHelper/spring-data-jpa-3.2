package com.example.controller;

import com.example.dto.ProductWithCategoryName;
import com.example.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void shouldReturnProductsAboveAvg() throws Exception {
        // given
        ProductWithCategoryName p1 = new ProductWithCategoryName(1L, "Laptop", new BigDecimal("1000"), "Electronics");
        ProductWithCategoryName p2 = new ProductWithCategoryName(2L, "Phone", new BigDecimal("800"), "Electronics");
        ProductWithCategoryName p3 = new ProductWithCategoryName(4L, "Textbook", new BigDecimal("50"), "Books");
        when(productService.getProductsAboveAvgPrice()).thenReturn(List.of(p1, p2, p3));

        // when & then
        mockMvc.perform(get("/api/products/above-avg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].name").value("Phone"))
                .andExpect(jsonPath("$[2].name").value("Textbook"));
    }
}