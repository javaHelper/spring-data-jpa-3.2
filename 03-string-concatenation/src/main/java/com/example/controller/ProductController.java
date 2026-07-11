package com.example.controller;

import com.example.dto.ProductInfo;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repository;

    @GetMapping("/{id}/info")
    public ProductInfo getProductInfo(@PathVariable Long id) {
        return repository.findProductInfoById(id);
    }

    @GetMapping("/search")
    public Product searchByFullText(@RequestParam String name, @RequestParam String description) {
        return repository.findByFullText(name + " " + description);
    }

    @GetMapping("/check")
    public Product checkIdAndVersion(@RequestParam Long id, @RequestParam Long version) {
        return repository.findByIdAndVersion(id, version);
    }
}