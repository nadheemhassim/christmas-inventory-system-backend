package com.example.inventory.repository;

import com.example.inventory.entity.Salesperson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalespersonRepository extends JpaRepository<Salesperson, Long> {
    List<Salesperson> findByNameContainingIgnoreCase(String name);

}
