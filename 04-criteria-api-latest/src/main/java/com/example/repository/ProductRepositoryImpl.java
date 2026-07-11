package com.example.repository;

import com.example.dto.ProductWithCategoryName;
import com.example.model.Category;
import com.example.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ProductWithCategoryName> findProductsAboveCategoryAveragePrice() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 1. Create a Tuple query (multiselect)
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        // 2. FROM Product p
        Root<Product> product = cq.from(Product.class);

        // 3. JOIN p.category c (explicit join)
        Join<Product, Category> categoryJoin = product.join("category", JoinType.INNER);

        // 4. Subquery: average price per category (correlated)
        Subquery<Double> subquery = cq.subquery(Double.class);
        Root<Product> subProduct = subquery.from(Product.class);
        subquery.select(cb.avg(subProduct.get("price")))
                .where(cb.equal(subProduct.get("category"), product.get("category")));

        // 5. SELECT p.id, p.name, p.price, c.name
        cq.multiselect(
                product.get("id"),
                product.get("name"),
                product.get("price"),
                categoryJoin.get("name")
        );

        // 6. WHERE p.price > subquery
        cq.where(cb.greaterThan(product.get("price"), subquery));

        // Execute
        TypedQuery<Tuple> query = em.createQuery(cq);
        List<Tuple> tuples = query.getResultList();

        // Convert Tuple list to DTO list
        return tuples.stream()
                .map(t -> new ProductWithCategoryName(
                        t.get(0, Long.class),
                        t.get(1, String.class),
                        t.get(2, BigDecimal.class),
                        t.get(3, String.class)
                ))
                .collect(Collectors.toList());
    }
}