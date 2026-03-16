package com.example.inventory.repository;

import com.example.inventory.entity.SellingPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellingPriceRepository extends JpaRepository<SellingPrice, Long> {
    // Find selling price by product ID
    Optional<SellingPrice> findByProductId(Long productId);

    // Find all selling prices for a list of product IDs
    List<SellingPrice> findByProductIdIn(List<Long> productIds);

    // Check if selling price exists for product
    boolean existsByProductId(Long productId);

    // Delete by product ID
    @Modifying
    @Query("DELETE FROM SellingPrice sp WHERE sp.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);

    // Update selling price for product
    @Modifying
    @Query("UPDATE SellingPrice sp SET sp.sellingPrice = :sellingPrice WHERE sp.productId = :productId")
    void updateSellingPrice(@Param("productId") Long productId, @Param("sellingPrice") Double sellingPrice);
}
