package com.example.repository;

import com.example.dto.ProductInfo;
import com.example.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest   // loads JPA repositories and uses embedded H2 (auto-configured)
@ActiveProfiles("test")  //
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        // Clear and pre-populate the database with test entities
        entityManager.clear();

        product1 = new Product("Laptop", "High-performance portable computer");
        product2 = new Product("Mouse", "Wireless ergonomic mouse");

        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.flush();
    }

    // ---------------------------------------------------------
    // 1. Test that || concatenation works in SELECT + id() + version()
    // ---------------------------------------------------------
    @Test
    void shouldReturnProductInfoWithConcatenationAndIdAndVersion() {
        // When
        ProductInfo info = productRepository.findProductInfoById(product1.getId());

        // Then
        assertThat(info).isNotNull();
        assertThat(info.getConcatenated()).isEqualTo("Laptop - High-performance portable computer");
        assertThat(info.getId()).isEqualTo(product1.getId());
        assertThat(info.getVersion()).isEqualTo(0L);  // initial version is 0
    }

    // ---------------------------------------------------------
    // 2. Test that || works in WHERE clause
    // ---------------------------------------------------------
    @Test
    void shouldFindProductByFullTextUsingConcatenationInWhere() {
        // Given
        String fullText = "Mouse Wireless ergonomic mouse";

        // When
        Product found = productRepository.findByFullText(fullText);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Mouse");
        assertThat(found.getDescription()).isEqualTo("Wireless ergonomic mouse");
    }

    // ---------------------------------------------------------
    // 3. Test that id() and version() work in WHERE clause
    // ---------------------------------------------------------
    @Test
    void shouldFindProductByIdAndVersion() {
        // Given
        Long id = product1.getId();
        Long version = product1.getVersion();  // 0

        // When
        Product found = productRepository.findByIdAndVersion(id, version);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(id);
        assertThat(found.getVersion()).isEqualTo(version);

        // And when using a wrong version, should return null (or empty)
        Product notFound = productRepository.findByIdAndVersion(id, version + 1);
        assertThat(notFound).isNull();
    }

    // ---------------------------------------------------------
    // 4. (Optional) Test that version increments after update
    //    to verify the @Version field behaviour
    // ---------------------------------------------------------
    @Test
    void shouldIncrementVersionAfterUpdate() {
        // Given
        Product original = productRepository.findById(product1.getId()).orElseThrow();
        assertThat(original.getVersion()).isEqualTo(0L);

        // When
        original.setName("Gaming Laptop");
        productRepository.save(original);
        entityManager.flush();
        entityManager.clear();

        // Then
        Product updated = productRepository.findById(product1.getId()).orElseThrow();
        assertThat(updated.getVersion()).isEqualTo(1L);  // version incremented

        // Now the version() function should return the new value
        ProductInfo info = productRepository.findProductInfoById(product1.getId());
        assertThat(info.getVersion()).isEqualTo(1L);
    }
}
