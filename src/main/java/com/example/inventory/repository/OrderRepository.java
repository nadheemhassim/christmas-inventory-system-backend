package com.example.inventory.repository;

import com.example.inventory.entity.Order;
import com.example.inventory.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.product.id = :productId")
    List<Order> findOrdersByProductId(@Param("productId") Long productId);


    // 🔹 NEW: Find orders by status
    List<Order> findByStatus(OrderStatus status);

    // 🔹 NEW: Find orders by customer
    List<Order> findByCustomerId(Long customerId);
}
