package com.example.inventory.repository;

import com.example.inventory.dto.StockReportDTO;
import com.example.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByInvoiceId(Long invoiceId);
    @Query("SELECT new com.example.inventory.dto.StockReportDTO(" +
            "p.id, p.itemNo, p.description, w.location, " +
            "p.totalProducts, p.inventoryQuantity, (p.totalProducts - p.inventoryQuantity), " +
            "p.cartons, p.boxesPerCarton, p.qtyPerBox, p.unitPrice, p.volume) " +
            "FROM Product p LEFT JOIN p.warehouse w " +
            "ORDER BY w.location, p.itemNo")
    List<StockReportDTO> getStockReport();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.warehouse WHERE p.id = :id")
    Optional<Product> findByIdWithWarehouse(@Param("id") Long id);


    // Simple query to get all products with warehouse eagerly fetched
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.warehouse w ORDER BY w.location, p.itemNo")
    List<Product> findAllWithWarehouse();

    // Or even simpler - just use the default findAll and handle DTO conversion in service
    List<Product> findAllByOrderByWarehouseLocationAscItemNoAsc();

    List<Product> findByItemNoContainingIgnoreCase(String itemNo);

    List<Product> findByDescriptionContainingIgnoreCase(String description);

    @Query("SELECT p FROM Product p WHERE p.itemNo LIKE %:searchTerm% OR p.description LIKE %:searchTerm%")
    List<Product> searchByItemNoOrDescription(@Param("searchTerm") String searchTerm);

    // Exact match for barcode scanning
    Optional<Product> findByItemNo(String itemNo);

    // For barcode prefix/suffix matching
    List<Product> findByItemNoStartingWith(String prefix);
    List<Product> findByItemNoEndingWith(String suffix);

    @Query("SELECT p FROM Product p WHERE p.inventoryQuantity > 0 AND (p.itemNo LIKE %:searchTerm% OR p.description LIKE %:searchTerm%)")
    List<Product> searchAvailableProducts(@Param("searchTerm") String searchTerm);

    // Check inventory for a specific product
    @Query("SELECT p.inventoryQuantity FROM Product p WHERE p.id = :productId")
    Integer getInventoryQuantity(@Param("productId") Long productId);

    @Modifying
    @Query(value = "UPDATE product SET inventory_quantity = :newQuantity WHERE id = :productId", nativeQuery = true)
    void updateInventoryNative(@Param("productId") Long productId, @Param("newQuantity") int newQuantity);


    // Add this method to clear the cache and get fresh data
    @Query("SELECT p.inventoryQuantity FROM Product p WHERE p.id = :productId")
    Integer getCurrentInventory(@Param("productId") Long productId);

    @Modifying
    @Query(value = "UPDATE product SET inventory_quantity = inventory_quantity - :quantity WHERE id = :productId", nativeQuery = true)
    void reduceInventory(@Param("productId") Long productId, @Param("quantity") int quantity);


}
