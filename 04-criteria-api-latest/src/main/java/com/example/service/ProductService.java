package com.example.service;

import com.example.dto.ProductWithCategoryName;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductWithCategoryName> getProductsAboveAvgPrice() {
        return productRepository.findProductsAboveCategoryAveragePrice();
    }
}