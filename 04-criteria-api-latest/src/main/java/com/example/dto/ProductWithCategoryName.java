package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductWithCategoryName {
    private Long id;
    private String name;
    private BigDecimal price;
    private String categoryName;
}