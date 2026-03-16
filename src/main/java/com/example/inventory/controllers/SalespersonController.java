package com.example.inventory.controllers;

import com.example.inventory.dto.SalespersonDTO;
import com.example.inventory.services.SalespersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salespersons")
@CrossOrigin(origins = "*")
public class SalespersonController {
    private final SalespersonService service;

    public SalespersonController(SalespersonService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SalespersonDTO> create(@RequestBody SalespersonDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public List<SalespersonDTO> getAll() {
        return service.getAll();
    }
}
