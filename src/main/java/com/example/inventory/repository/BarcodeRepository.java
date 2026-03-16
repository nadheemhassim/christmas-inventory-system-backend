package com.example.inventory.repository;

import com.example.inventory.entity.Barcode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BarcodeRepository extends JpaRepository<Barcode, Long> {
    Optional<Barcode> findByBarcode(String barcode);
    Optional<Barcode> findByBarcodeAndProductId(String barcode, Long productId);
    boolean existsByBarcode(String barcode);
}
