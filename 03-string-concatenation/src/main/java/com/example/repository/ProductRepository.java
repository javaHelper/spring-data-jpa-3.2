package com.example.repository;

import com.example.dto.ProductInfo;
import com.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Use the correct fully qualified name: com.example.demo.ProductInfo
    @Query("""
            SELECT new com.example.dto.ProductInfo(p.name || ' - ' || p.description, id(p), version(p))
            FROM Product p WHERE p.id = :id
            """)
    ProductInfo findProductInfoById(@Param("id") Long id);

    @Query("SELECT p FROM Product p WHERE p.name || ' ' || p.description = :fullText")
    Product findByFullText(@Param("fullText") String fullText);

    @Query("SELECT p FROM Product p WHERE id(p) = :id AND version(p) = :version")
    Product findByIdAndVersion(@Param("id") Long id, @Param("version") Long version);
}