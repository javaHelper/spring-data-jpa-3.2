package com.example.repository;

import com.example.dto.ProductWithCategoryName;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductWithCategoryName> findProductsAboveCategoryAveragePrice();
}