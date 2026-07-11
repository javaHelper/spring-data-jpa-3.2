package com.example.controller.service;

import com.example.controller.dto.ProductDTO;
import com.example.controller.model.Product;
import com.example.controller.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    // ============ CRUD OPERATIONS ============

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setCode(productDetails.getCode());
                    product.setCategory(productDetails.getCategory());
                    product.setPrice(productDetails.getPrice());
                    product.setStatus(productDetails.getStatus());
                    product.setStock(productDetails.getStock());
                    product.setSupplier(productDetails.getSupplier());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // ============ DTO CONVERSION METHODS ============

    public ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getCode(),
                product.getCategory(),
                product.getPrice(),
                product.getStatus()
        );
    }

    public List<ProductDTO> convertToDTOList(List<Product> products) {
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ============ UNION QUERY METHODS ============

    public List<Product> getActiveAndDiscontinued() {
        return productRepository.findActiveAndDiscontinued();
    }

    public List<Product> getCategoryOrExpensiveProducts(String category, BigDecimal minPrice) {
        return productRepository.findCategoryOrExpensiveProducts(category, minPrice);
    }

    // ============ INTERSECT QUERY METHODS ============

    public List<Product> getActiveInStockProducts() {
        return productRepository.findActiveInStockProducts();
    }

    public List<Product> getProductsBySupplierAndCategory(String supplier, String category) {
        return productRepository.findProductsBySupplierAndCategory(supplier, category);
    }

    // ============ EXCEPT QUERY METHODS ============

    public List<Product> getProductsExcludingDiscontinued() {
        return productRepository.findProductsExcludingDiscontinued();
    }

    public List<Product> getProductsOutOfStock() {
        return productRepository.findProductsOutOfStock();
    }

    // ============ CAST QUERY METHODS ============

    public List<Product> getProductsByPriceCast(Integer minPrice) {
        return productRepository.findProductsByPriceCast(minPrice);
    }

    public List<Product> getProductsByCodeLength(Integer codeLength) {
        return productRepository.findProductsByCodeLength(codeLength);
    }

    // ============ LEFT QUERY METHODS ============

    public List<Product> getProductsByCodePrefix(String prefix) {
        return productRepository.findProductsByCodePrefix(prefix);
    }

    // ============ RIGHT QUERY METHODS ============

    public List<Product> getProductsByCodeSuffix(String suffix) {
        return productRepository.findProductsByCodeSuffix(suffix);
    }

    // ============ REPLACE QUERY METHODS ============

    public List<Product> getProductsWithReplacedDescription(String oldText, String newText) {
        return productRepository.findProductsWithReplacedDescription(oldText, newText);
    }

    public List<Product> getProductsWithReplacedName(String searchText, String replaceText) {
        return productRepository.findProductsWithReplacedName(searchText, replaceText);
    }

    // ============ COMPLEX QUERY METHODS ============

    public List<Product> getProductsWithComplexOperations(Integer minPrice, String firstChar) {
        return productRepository.findProductsWithComplexOperations(minPrice, firstChar);
    }
}
