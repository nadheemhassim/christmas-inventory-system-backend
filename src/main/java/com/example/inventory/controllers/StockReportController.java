package com.example.inventory.controllers;

import com.example.inventory.dto.StockReportDTO;
import com.example.inventory.services.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin(origins = "http://localhost:3000")
public class StockReportController {

    private final StockService stockService;

    public StockReportController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/report")
    public ResponseEntity<List<StockReportDTO>> getStockReport() {
        List<StockReportDTO> report = stockService.getStockReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/products")
    public ResponseEntity<List<StockReportDTO>> getAllProductsForStock() {
        List<StockReportDTO> products = stockService.getAllProductsForStockReport();
        return ResponseEntity.ok(products);
    }
}
