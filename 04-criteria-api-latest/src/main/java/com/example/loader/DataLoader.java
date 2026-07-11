package com.example.loader;

import com.example.model.Category;
import com.example.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Create categories
        Category electronics = new Category("Electronics");
        Category books = new Category("Books");
        em.persist(electronics);
        em.persist(books);

        // Create products
        em.persist(new Product("Laptop", new BigDecimal("1000.00"), electronics));
        em.persist(new Product("Phone", new BigDecimal("800.00"), electronics));
        em.persist(new Product("Novel", new BigDecimal("20.00"), books));
        em.persist(new Product("Textbook", new BigDecimal("50.00"), books));

        // Also add one more to show average effect: a cheap electronics item
        em.persist(new Product("Charger", new BigDecimal("30.00"), electronics));

        em.flush();
    }
}