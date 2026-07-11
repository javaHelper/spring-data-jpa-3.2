package com.example.service;


import com.example.controller.dto.ProductDTO;
import com.example.controller.model.Product;
import com.example.controller.repository.ProductRepository;
import com.example.controller.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private List<Product> testProducts;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Laptop");
        testProduct.setCode("LAP001");
        testProduct.setCategory("Electronics");
        testProduct.setPrice(new BigDecimal("1200.00"));
        testProduct.setStock(50);
        testProduct.setStatus("ACTIVE");
        testProduct.setSupplier("TechCorp");
        testProduct.setDescription("High-performance laptop");

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("Office Chair");
        p2.setCode("CHR002");
        p2.setCategory("Furniture");
        p2.setPrice(new BigDecimal("350.00"));
        p2.setStock(120);
        p2.setStatus("ACTIVE");
        p2.setSupplier("FurniturePlus");

        testProducts = List.of(testProduct, p2);
    }

    // ============ CRUD TEST CASES ============

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product created = productService.createProduct(testProduct);

        assertNotNull(created);
        assertEquals("Test Laptop", created.getName());
        assertEquals("LAP001", created.getCode());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Optional<Product> found = productService.getProductById(1L);

        assertTrue(found.isPresent());
        assertEquals("Test Laptop", found.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Product> found = productService.getProductById(999L);

        assertFalse(found.isPresent());
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(testProducts);

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Laptop");
        updatedProduct.setPrice(new BigDecimal("1500.00"));
        updatedProduct.setStatus("ACTIVE");

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    // ============ DTO CONVERSION TESTS ============

    @Test
    void testConvertToDTO() {
        ProductDTO dto = productService.convertToDTO(testProduct);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Laptop", dto.getName());
        assertEquals("LAP001", dto.getCode());
        assertEquals("Electronics", dto.getCategory());
        assertEquals(new BigDecimal("1200.00"), dto.getPrice());
        assertEquals("ACTIVE", dto.getStatus());
    }

    @Test
    void testConvertToDTOList() {
        List<ProductDTO> dtos = productService.convertToDTOList(testProducts);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("Test Laptop", dtos.get(0).getName());
        assertEquals("Office Chair", dtos.get(1).getName());
    }

    // ============ UNION QUERY TESTS ============

    @Test
    void testGetActiveAndDiscontinued() {
        when(productRepository.findActiveAndDiscontinued()).thenReturn(testProducts);

        List<Product> products = productService.getActiveAndDiscontinued();

        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findActiveAndDiscontinued();
    }

    @Test
    void testGetCategoryOrExpensiveProducts() {
        when(productRepository.findCategoryOrExpensiveProducts("Electronics", new BigDecimal("1000")))
            .thenReturn(List.of(testProduct));

        List<Product> products = productService.getCategoryOrExpensiveProducts("Electronics", new BigDecimal("1000"));

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findCategoryOrExpensiveProducts("Electronics", new BigDecimal("1000"));
    }

    // ============ INTERSECT QUERY TESTS ============

    @Test
    void testGetActiveInStockProducts() {
        when(productRepository.findActiveInStockProducts()).thenReturn(testProducts);

        List<Product> products = productService.getActiveInStockProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertTrue(products.stream().allMatch(p -> "ACTIVE".equals(p.getStatus())));
        verify(productRepository, times(1)).findActiveInStockProducts();
    }

    @Test
    void testGetProductsBySupplierAndCategory() {
        when(productRepository.findProductsBySupplierAndCategory("TechCorp", "Electronics"))
            .thenReturn(List.of(testProduct));

        List<Product> products = productService.getProductsBySupplierAndCategory("TechCorp", "Electronics");

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("TechCorp", products.get(0).getSupplier());
        verify(productRepository, times(1)).findProductsBySupplierAndCategory("TechCorp", "Electronics");
    }

    // ============ EXCEPT QUERY TESTS ============

    @Test
    void testGetProductsExcludingDiscontinued() {
        when(productRepository.findProductsExcludingDiscontinued()).thenReturn(testProducts);

        List<Product> products = productService.getProductsExcludingDiscontinued();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertTrue(products.stream().noneMatch(p -> "DISCONTINUED".equals(p.getStatus())));
        verify(productRepository, times(1)).findProductsExcludingDiscontinued();
    }

    @Test
    void testGetProductsOutOfStock() {
        Product outOfStockProduct = new Product();
        outOfStockProduct.setId(3L);
        outOfStockProduct.setName("Out of Stock Item");
        outOfStockProduct.setStock(0);

        when(productRepository.findProductsOutOfStock()).thenReturn(List.of(outOfStockProduct));

        List<Product> products = productService.getProductsOutOfStock();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(0, products.get(0).getStock());
        verify(productRepository, times(1)).findProductsOutOfStock();
    }

    // ============ CAST QUERY TESTS ============

    @Test
    void testGetProductsByPriceCast() {
        when(productRepository.findProductsByPriceCast(1000)).thenReturn(List.of(testProduct));

        List<Product> products = productService.getProductsByPriceCast(1000);

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findProductsByPriceCast(1000);
    }

    @Test
    void testGetProductsByCodeLength() {
        when(productRepository.findProductsByCodeLength(6)).thenReturn(testProducts);

        List<Product> products = productService.getProductsByCodeLength(6);

        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findProductsByCodeLength(6);
    }

    // ============ LEFT/RIGHT QUERY TESTS ============

    @Test
    void testGetProductsByCodePrefix() {
        when(productRepository.findProductsByCodePrefix("LAP")).thenReturn(List.of(testProduct));

        List<Product> products = productService.getProductsByCodePrefix("LAP");

        assertNotNull(products);
        assertEquals(1, products.size());
        assertTrue(products.get(0).getCode().startsWith("LAP"));
        verify(productRepository, times(1)).findProductsByCodePrefix("LAP");
    }

    @Test
    void testGetProductsByCodeSuffix() {
        when(productRepository.findProductsByCodeSuffix("001")).thenReturn(List.of(testProduct));

        List<Product> products = productService.getProductsByCodeSuffix("001");

        assertNotNull(products);
        assertEquals(1, products.size());
        assertTrue(products.get(0).getCode().endsWith("001"));
        verify(productRepository, times(1)).findProductsByCodeSuffix("001");
    }

    // ============ REPLACE QUERY TESTS ============

    @Test
    void testGetProductsWithReplacedDescription() {
        when(productRepository.findProductsWithReplacedDescription("laptop", "computer"))
            .thenReturn(List.of(testProduct));

        List<Product> products = productService.getProductsWithReplacedDescription("laptop", "computer");

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findProductsWithReplacedDescription("laptop", "computer");
    }

    @Test
    void testGetProductsWithReplacedName() {
        when(productRepository.findProductsWithReplacedName("Test", "Updated"))
            .thenReturn(List.of(testProduct));

        List<Product> products = productService.getProductsWithReplacedName("Test", "Updated");

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findProductsWithReplacedName("Test", "Updated");
    }

    // ============ COMPLEX QUERY TESTS ============

    @Test
    void testGetProductsWithComplexOperations() {
        when(productRepository.findProductsWithComplexOperations(200, "L"))
            .thenReturn(List.of(testProduct));

        List<Product> products = productService.getProductsWithComplexOperations(200, "L");

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findProductsWithComplexOperations(200, "L");
    }
}