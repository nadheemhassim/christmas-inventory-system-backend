package com.example.inventory.repository;

import com.example.inventory.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
