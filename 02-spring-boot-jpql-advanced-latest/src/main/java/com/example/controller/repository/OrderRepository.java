package com.example.controller.repository;

import com.example.controller.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // UNION - Pending and Cancelled orders
    @Query(value = """
        SELECT * FROM orders o WHERE o.status = 'PENDING'
        UNION
        SELECT * FROM orders o WHERE o.status = 'CANCELLED'
        ORDER BY order_date DESC
    """, nativeQuery = true)
    List<Order> findPendingOrCancelledOrders();

    // INTERSECT - Orders that are confirmed AND paid
    @Query(value = """
        SELECT * FROM orders o WHERE o.status = 'CONFIRMED'
        INTERSECT
        SELECT * FROM orders o WHERE o.payment_status = 'PAID'
        ORDER BY order_date DESC
    """, nativeQuery = true)
    List<Order> findConfirmedPaidOrders();

    // EXCEPT - All orders except delivered
    @Query(value = """
        SELECT * FROM orders o
        EXCEPT
        SELECT * FROM orders o WHERE o.status = 'DELIVERED'
        ORDER BY order_date DESC
    """, nativeQuery = true)
    List<Order> findUndeliveredOrders();

    // CAST - Find orders with cast total amount
    @Query(value = """
        SELECT * FROM orders o
        WHERE CAST(o.total_amount AS integer) > :minAmount
        ORDER BY total_amount DESC
    """, nativeQuery = true)
    List<Order> findOrdersByMinAmount(@Param("minAmount") Integer minAmount);
}