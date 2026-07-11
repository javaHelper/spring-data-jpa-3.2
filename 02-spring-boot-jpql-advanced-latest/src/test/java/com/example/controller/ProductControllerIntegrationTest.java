package com.example.controller;

import com.example.controller.model.Product;
import com.example.controller.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        // Create ACTIVE products
        Product p1 = new Product();
        p1.setName("Laptop Professional");
        p1.setDescription("High-performance business laptop");
        p1.setCode("LAP001");
        p1.setCategory("Electronics");
        p1.setPrice(new BigDecimal("1200.00"));
        p1.setStock(50);
        p1.setStatus("ACTIVE");
        p1.setSupplier("TechCorp");

        Product p2 = new Product();
        p2.setName("Office Chair");
        p2.setDescription("Ergonomic office chair for comfort");
        p2.setCode("CHR002");
        p2.setCategory("Furniture");
        p2.setPrice(new BigDecimal("350.00"));
        p2.setStock(120);
        p2.setStatus("ACTIVE");
        p2.setSupplier("FurniturePlus");

        // Create OUT OF STOCK product
        Product p3 = new Product();
        p3.setName("Desk Monitor");
        p3.setDescription("27 inch 4K monitor for work");
        p3.setCode("MON003");
        p3.setCategory("Electronics");
        p3.setPrice(new BigDecimal("400.00"));
        p3.setStock(0);
        p3.setStatus("ACTIVE");
        p3.setSupplier("TechCorp");

        // Create DISCONTINUED product
        Product p4 = new Product();
        p4.setName("CRT Monitor");
        p4.setDescription("Old CRT monitor discontinued");
        p4.setCode("CRT005");
        p4.setCategory("Electronics");
        p4.setPrice(new BigDecimal("50.00"));
        p4.setStock(0);
        p4.setStatus("DISCONTINUED");
        p4.setSupplier("TechCorp");

        // Create EXPENSIVE product
        Product p5 = new Product();
        p5.setName("Expensive Keyboard");
        p5.setDescription("Premium mechanical keyboard");
        p5.setCode("KEY006");
        p5.setCategory("Accessories");
        p5.setPrice(new BigDecimal("1500.00"));
        p5.setStock(10);
        p5.setStatus("ACTIVE");
        p5.setSupplier("KeyMaster");

        // Create INACTIVE product
        Product p6 = new Product();
        p6.setName("Old Printer");
        p6.setDescription("Deprecated printer model");
        p6.setCode("PRI004");
        p6.setCategory("Electronics");
        p6.setPrice(new BigDecimal("250.00"));
        p6.setStock(5);
        p6.setStatus("INACTIVE");
        p6.setSupplier("OfficePlus");

        productRepository.saveAll(java.util.List.of(p1, p2, p3, p4, p5, p6));
    }

    // ============ CRUD TESTS ============

    @Test
    void testCreateProduct() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setDescription("Test product");
        newProduct.setCode("NEW001");
        newProduct.setCategory("Test");
        newProduct.setPrice(new BigDecimal("99.99"));
        newProduct.setStock(10);
        newProduct.setStatus("ACTIVE");
        newProduct.setSupplier("TestSupplier");

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Product")))
                .andExpect(jsonPath("$.code", is("NEW001")));
    }

    @Test
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].name", is(notNullValue())));
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = productRepository.findAll().get(0);
        Long productId = product.getId();

        mockMvc.perform(get("/api/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId.intValue())))
                .andExpect(jsonPath("$.name", is(product.getName())));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product product = productRepository.findAll().get(0);
        Long productId = product.getId();

        product.setName("Updated Laptop");
        product.setPrice(new BigDecimal("1500.00"));

        mockMvc.perform(put("/api/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Laptop")))
                .andExpect(jsonPath("$.price", is(1500.00)));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Product product = productRepository.findAll().get(0);
        Long productId = product.getId();

        mockMvc.perform(delete("/api/products/" + productId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/" + productId))
                .andExpect(status().isNotFound());
    }

    // ============ UNION TESTS ============

    @Test
    void testGetActiveAndDiscontinued() throws Exception {
        mockMvc.perform(get("/api/products/union/active-discontinued")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[*].status", hasItems("ACTIVE", "DISCONTINUED")));
    }

    @Test
    void testGetCategoryOrExpensiveProducts() throws Exception {
        mockMvc.perform(get("/api/products/union/category-or-expensive")
                        .param("category", "Electronics")
                        .param("minPrice", "1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    // ============ INTERSECT TESTS ============

    @Test
    void testGetActiveInStockProducts() throws Exception {
        mockMvc.perform(get("/api/products/intersect/active-instock")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[*].status", everyItem(is("ACTIVE"))));
    }

    @Test
    void testGetProductsBySupplierAndCategory() throws Exception {
        mockMvc.perform(get("/api/products/intersect/supplier-category")
                        .param("supplier", "TechCorp")
                        .param("category", "Electronics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // ============ EXCEPT TESTS ============

    @Test
    void testGetProductsExcludingDiscontinued() throws Exception {
        mockMvc.perform(get("/api/products/except/excluding-discontinued")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[*].status",
                        everyItem(not(is("DISCONTINUED")))));
    }

    @Test
    void testGetProductsOutOfStock() throws Exception {
        mockMvc.perform(get("/api/products/except/out-of-stock")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // ============ CAST TESTS ============

    @Test
    void testGetProductsByPriceCast() throws Exception {
        mockMvc.perform(get("/api/products/cast/by-price")
                        .param("minPrice", "300")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void testGetProductsByCodeLength() throws Exception {
        mockMvc.perform(get("/api/products/cast/by-code-length")
                        .param("codeLength", "6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // ============ LEFT TESTS ============

    @Test
    void testGetProductsByCodePrefix() throws Exception {
        mockMvc.perform(get("/api/products/left/by-code-prefix")
                        .param("prefix", "LAP")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductsByCodePrefixNoMatch() throws Exception {
        mockMvc.perform(get("/api/products/left/by-code-prefix")
                        .param("prefix", "XYZ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ============ RIGHT TESTS ============

    @Test
    void testGetProductsByCodeSuffix() throws Exception {
        mockMvc.perform(get("/api/products/right/by-code-suffix")
                        .param("suffix", "001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // ============ REPLACE TESTS ============

    @Test
    void testGetProductsWithReplacedDescription() throws Exception {
        mockMvc.perform(get("/api/products/replace/description")
                        .param("oldText", "laptop")
                        .param("newText", "computer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductsWithReplacedName() throws Exception {
        mockMvc.perform(get("/api/products/replace/name")
                        .param("searchText", "Professional")
                        .param("replaceText", "Business")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // ============ COMPLEX OPERATION TESTS ============

    @Test
    void testGetProductsWithComplexOperations() throws Exception {
        mockMvc.perform(get("/api/products/complex")
                        .param("minPrice", "200")
                        .param("firstChar", "L")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}