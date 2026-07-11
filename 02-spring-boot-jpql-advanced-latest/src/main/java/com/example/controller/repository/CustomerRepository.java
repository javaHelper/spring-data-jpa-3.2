package com.example.controller.repository;

import com.example.controller.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // UNION - VIP and Premium customers
    @Query(value = """
        SELECT * FROM customers c WHERE c.status = 'VIP'
        UNION
        SELECT * FROM customers c WHERE c.status = 'PREMIUM'
        ORDER BY first_name ASC
    """, nativeQuery = true)
    List<Customer> findVipAndPremiumCustomers();

    // UNION - Multiple cities
    @Query(value = """
        SELECT * FROM customers c WHERE c.city = :city1
        UNION
        SELECT * FROM customers c WHERE c.city = :city2
        ORDER BY city ASC
    """, nativeQuery = true)
    List<Customer> findCustomersFromMultipleCities(@Param("city1") String city1, @Param("city2") String city2);

    // INTERSECT - Active customers AND high spenders
    @Query(value = """
        SELECT * FROM customers c WHERE c.status = 'ACTIVE'
        INTERSECT
        SELECT * FROM customers c WHERE c.total_spent > :minSpent
        ORDER BY first_name ASC
    """, nativeQuery = true)
    List<Customer> findActiveHighSpenders(@Param("minSpent") Double minSpent);

    // INTERSECT - Customers by country AND status
    @Query(value = """
        SELECT * FROM customers c WHERE c.country = :country
        INTERSECT
        SELECT * FROM customers c WHERE c.status = :status
        ORDER BY first_name ASC
    """, nativeQuery = true)
    List<Customer> findCustomersByCountryAndStatus(@Param("country") String country, @Param("status") String status);

    // EXCEPT - All customers except inactive
    @Query(value = """
        SELECT * FROM customers c
        EXCEPT
        SELECT * FROM customers c WHERE c.status = 'INACTIVE'
        ORDER BY first_name ASC
    """, nativeQuery = true)
    List<Customer> findActiveCustomers();

    // EXCEPT - Customers not from specific country
    @Query(value = """
        SELECT * FROM customers c
        EXCEPT
        SELECT * FROM customers c WHERE c.country = :excludeCountry
        ORDER BY first_name ASC
    """, nativeQuery = true)
    List<Customer> findCustomersNotFromCountry(@Param("excludeCountry") String excludeCountry);

    // CAST - Convert totalSpent to Integer
    @Query(value = """
        SELECT * FROM customers c
        WHERE CAST(c.total_spent AS integer) > :minSpentInt
        ORDER BY total_spent DESC
    """, nativeQuery = true)
    List<Customer> findCustomersBySpentAmount(@Param("minSpentInt") Integer minSpentInt);

    // LEFT - Extract first N characters from email
    @Query(value = """
        SELECT * FROM customers c
        WHERE LEFT(c.email, 3) = :emailPrefix
        ORDER BY email ASC
    """, nativeQuery = true)
    List<Customer> findCustomersByEmailPrefix(@Param("emailPrefix") String emailPrefix);

    // RIGHT - Extract domain from email
    @Query(value = """
        SELECT * FROM customers c
        WHERE RIGHT(c.email, 10) = :emailDomain
        ORDER BY email ASC
    """, nativeQuery = true)
    List<Customer> findCustomersByEmailDomain(@Param("emailDomain") String emailDomain);

    // REPLACE - Replace text in customer name
    @Query(value = """
        SELECT * FROM customers c
        WHERE c.first_name LIKE CONCAT('%', :oldName, '%')
        ORDER BY first_name ASC
    """, nativeQuery = true)
    List<Customer> findCustomersWithReplacedFirstName(@Param("oldName") String oldName, @Param("newName") String newName);

    // UNION + CAST - Combine VIP and Premium
    @Query(value = """
        SELECT * FROM customers c
        WHERE c.status = 'VIP' AND CAST(c.total_spent AS integer) > 1000
        UNION
        SELECT * FROM customers c
        WHERE c.status = 'PREMIUM' AND CAST(c.total_spent AS integer) > 500
        ORDER BY first_name ASC
    """, nativeQuery = true)
    List<Customer> findQualifiedVipAndPremium();
}