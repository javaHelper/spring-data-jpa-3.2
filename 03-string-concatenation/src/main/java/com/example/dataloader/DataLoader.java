package com.example.dataloader;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository repository;

    @Override
    public void run(String... args) {
        repository.save(new Product("Laptop", "High-performance portable computer"));
        repository.save(new Product("Mouse", "Wireless ergonomic mouse"));
    }
}