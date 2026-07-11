package com.example.repository;

import com.example.dto.ProductWithCategoryName;
import com.example.model.Category;
import com.example.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void shouldReturnProductsAboveCategoryAverage() {
        // given (data already inserted by DataLoader, but we can also set up here)
        // For independence, we'll clear and insert our own data.
        em.createQuery("DELETE FROM Product").executeUpdate();
        em.createQuery("DELETE FROM Category").executeUpdate();

        Category electronics = new Category("Electronics");
        Category books = new Category("Books");
        em.persist(electronics);
        em.persist(books);

        em.persist(new Product("Laptop", new BigDecimal("1000"), electronics));
        em.persist(new Product("Phone", new BigDecimal("800"), electronics));
        em.persist(new Product("Charger", new BigDecimal("30"), electronics));
        em.persist(new Product("Novel", new BigDecimal("20"), books));
        em.persist(new Product("Textbook", new BigDecimal("50"), books));
        em.flush();

        // when
        List<ProductWithCategoryName> result = productRepository.findProductsAboveCategoryAveragePrice();

        // then
        assertThat(result).hasSize(3);
        assertThat(result).extracting("name").containsExactlyInAnyOrder("Laptop", "Phone", "Textbook");
        assertThat(result).extracting("categoryName").containsOnly("Electronics", "Electronics", "Books");
    }
}