package com.example.controller.repository;

import com.example.controller.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ============ UNION QUERIES ============

    @Query("""
        SELECT p FROM Product p WHERE p.status = 'ACTIVE'
        UNION
        SELECT p FROM Product p WHERE p.status = 'DISCONTINUED'
        ORDER BY p.name ASC
    """)
    List<Product> findActiveAndDiscontinued();

    @Query("""
        SELECT p FROM Product p WHERE p.category = :category AND p.status = 'ACTIVE'
        UNION
        SELECT p FROM Product p WHERE p.price > :minPrice AND p.status = 'ACTIVE'
        ORDER BY p.price DESC
    """)
    List<Product> findCategoryOrExpensiveProducts(
            @Param("category") String category,
            @Param("minPrice") BigDecimal minPrice
    );

    // ============ INTERSECT QUERIES ============

    @Query("""
        SELECT p FROM Product p WHERE p.status = 'ACTIVE'
        INTERSECT
        SELECT p FROM Product p WHERE p.stock > 0
        ORDER BY p.name ASC
    """)
    List<Product> findActiveInStockProducts();

    @Query("""
        SELECT p FROM Product p WHERE p.supplier = :supplier
        INTERSECT
        SELECT p FROM Product p WHERE p.category = :category
        ORDER BY p.name ASC
    """)
    List<Product> findProductsBySupplierAndCategory(
            @Param("supplier") String supplier,
            @Param("category") String category
    );

    // ============ EXCEPT QUERIES ============

    @Query("""
        SELECT p FROM Product p WHERE p.status IN ('ACTIVE', 'INACTIVE')
        EXCEPT
        SELECT p FROM Product p WHERE p.status = 'DISCONTINUED'
        ORDER BY p.name ASC
    """)
    List<Product> findProductsExcludingDiscontinued();

    @Query("""
        SELECT p FROM Product p
        EXCEPT
        SELECT p FROM Product p WHERE p.stock > 0
        ORDER BY p.name ASC
    """)
    List<Product> findProductsOutOfStock();

    // ============ CAST QUERIES ============

    @Query("""
        SELECT p FROM Product p
        WHERE CAST(p.price AS integer) > :minIntPrice
        ORDER BY p.price DESC
    """)
    List<Product> findProductsByPriceCast(@Param("minIntPrice") Integer minIntPrice);

    @Query("""
        SELECT p FROM Product p
        WHERE CAST(LENGTH(p.code) AS integer) = :codeLength
        ORDER BY p.code ASC
    """)
    List<Product> findProductsByCodeLength(@Param("codeLength") Integer codeLength);

    // ============ LEFT QUERIES ============

    @Query("""
        SELECT p FROM Product p
        WHERE LEFT(p.code, 3) = :prefix
        ORDER BY p.name ASC
    """)
    List<Product> findProductsByCodePrefix(@Param("prefix") String prefix);

    // ============ RIGHT QUERIES ============

    @Query("""
        SELECT p FROM Product p
        WHERE RIGHT(p.code, 3) = :suffix
        ORDER BY p.name ASC
    """)
    List<Product> findProductsByCodeSuffix(@Param("suffix") String suffix);

    // ============ REPLACE QUERIES ============

    @Query("""
        SELECT p FROM Product p
        WHERE p.description LIKE CONCAT('%', :oldText, '%')
        ORDER BY p.name ASC
    """)
    List<Product> findProductsWithReplacedDescription(
            @Param("oldText") String oldText,
            @Param("newText") String newText
    );

    @Query("""
        SELECT p FROM Product p
        WHERE p.name LIKE CONCAT('%', :searchText, '%')
        ORDER BY p.name ASC
    """)
    List<Product> findProductsWithReplacedName(
            @Param("searchText") String searchText,
            @Param("replaceText") String replaceText
    );

    // ============ COMPLEX QUERIES ============

    @Query("""
        SELECT p FROM Product p
        WHERE CAST(p.price AS integer) > :minPrice
        AND LEFT(p.code, 1) = :firstChar
        ORDER BY p.price DESC
    """)
    List<Product> findProductsWithComplexOperations(
            @Param("minPrice") Integer minPrice,
            @Param("firstChar") String firstChar
    );
}