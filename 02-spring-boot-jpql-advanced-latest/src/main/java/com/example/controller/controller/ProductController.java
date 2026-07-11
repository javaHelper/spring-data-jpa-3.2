package com.example.controller.controller;

import com.example.controller.dto.ProductDTO;
import com.example.controller.model.Product;
import com.example.controller.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> new ResponseEntity<>(productService.convertToDTO(product), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    // ============ UNION ENDPOINTS ============

    @GetMapping("/union/active-discontinued")
    public ResponseEntity<List<ProductDTO>> getActiveAndDiscontinued() {
        List<Product> products = productService.getActiveAndDiscontinued();
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    @GetMapping("/union/category-or-expensive")
    public ResponseEntity<List<ProductDTO>> getCategoryOrExpensiveProducts(
            @RequestParam String category,
            @RequestParam BigDecimal minPrice) {
        List<Product> products = productService.getCategoryOrExpensiveProducts(category, minPrice);
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    // ============ INTERSECT ENDPOINTS ============

    @GetMapping("/intersect/active-instock")
    public ResponseEntity<List<ProductDTO>> getActiveInStockProducts() {
        List<Product> products = productService.getActiveInStockProducts();
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    @GetMapping("/intersect/supplier-category")
    public ResponseEntity<List<ProductDTO>> getProductsBySupplierAndCategory(
            @RequestParam String supplier,
            @RequestParam String category) {
        List<Product> products = productService.getProductsBySupplierAndCategory(supplier, category);
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    // ============ EXCEPT ENDPOINTS ============

    @GetMapping("/except/excluding-discontinued")
    public ResponseEntity<List<ProductDTO>> getProductsExcludingDiscontinued() {
        List<Product> products = productService.getProductsExcludingDiscontinued();
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    @GetMapping("/except/out-of-stock")
    public ResponseEntity<List<ProductDTO>> getProductsOutOfStock() {
        List<Product> products = productService.getProductsOutOfStock();
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    // ============ CAST ENDPOINTS ============

    @GetMapping("/cast/by-price")
    public ResponseEntity<List<ProductDTO>> getProductsByPriceCast(@RequestParam Integer minPrice) {
        List<Product> products = productService.getProductsByPriceCast(minPrice);
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    @GetMapping("/cast/by-code-length")
    public ResponseEntity<List<ProductDTO>> getProductsByCodeLength(@RequestParam Integer codeLength) {
        List<Product> products = productService.getProductsByCodeLength(codeLength);
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    // ============ LEFT ENDPOINT ============

    @GetMapping("/left/by-code-prefix")
    public ResponseEntity<List<ProductDTO>> getProductsByCodePrefix(@RequestParam String prefix) {
        List<Product> products = productService.getProductsByCodePrefix(prefix);
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    // ============ RIGHT ENDPOINT ============

    @GetMapping("/right/by-code-suffix")
    public ResponseEntity<List<ProductDTO>> getProductsByCodeSuffix(@RequestParam String suffix) {
        List<Product> products = productService.getProductsByCodeSuffix(suffix);
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    // ============ REPLACE ENDPOINTS ============

    @GetMapping("/replace/description")
    public ResponseEntity<List<ProductDTO>> getProductsWithReplacedDescription(
            @RequestParam String oldText,
            @RequestParam String newText) {
        List<Product> products = productService.getProductsWithReplacedDescription(oldText, newText);
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    @GetMapping("/replace/name")
    public ResponseEntity<List<ProductDTO>> getProductsWithReplacedName(
            @RequestParam String searchText,
            @RequestParam String replaceText) {
        List<Product> products = productService.getProductsWithReplacedName(searchText, replaceText);
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    // ============ COMPLEX OPERATIONS ENDPOINT ============

    @GetMapping("/complex")
    public ResponseEntity<List<ProductDTO>> getProductsWithComplexOperations(
            @RequestParam Integer minPrice,
            @RequestParam String firstChar) {
        List<Product> products = productService.getProductsWithComplexOperations(minPrice, firstChar);
        return new ResponseEntity<>(productService.convertToDTOList(products), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return new ResponseEntity<>(productService.updateProduct(id, productDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}