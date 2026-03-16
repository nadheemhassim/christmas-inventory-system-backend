package com.example.inventory.controllers;

import com.example.inventory.entity.SellingPrice;
import com.example.inventory.services.SellingPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/selling-prices")
@CrossOrigin(origins = "*")
public class SellingPriceController {
    @Autowired
    private SellingPriceService sellingPriceService;

    // GET all selling prices
    @GetMapping
    public ResponseEntity<List<SellingPrice>> getAllSellingPrices() {
        try {
            List<SellingPrice> sellingPrices = sellingPriceService.getAllSellingPrices();
            return ResponseEntity.ok(sellingPrices);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // GET selling price by product ID
    @GetMapping("/product/{productId}")
    public ResponseEntity<SellingPrice> getSellingPriceByProductId(@PathVariable Long productId) {
        try {
            SellingPrice sellingPrice = sellingPriceService.getSellingPriceByProductId(productId);
            if (sellingPrice != null) {
                return ResponseEntity.ok(sellingPrice);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // POST save or update single selling price
    @PostMapping
    public ResponseEntity<SellingPrice> saveSellingPrice(@RequestBody Map<String, Object> request) {
        try {
            Long productId = ((Number) request.get("productId")).longValue();
            Double sellingPrice = ((Number) request.get("sellingPrice")).doubleValue();

            SellingPrice savedPrice = sellingPriceService.saveSellingPrice(productId, sellingPrice);
            return ResponseEntity.ok(savedPrice);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // POST bulk update selling prices
    @PostMapping("/bulk")
    public ResponseEntity<List<SellingPrice>> bulkUpdateSellingPrices(@RequestBody List<Map<String, Object>> requests) {
        try {
            List<SellingPrice> savedPrices = sellingPriceService.bulkUpdateSellingPrices(requests);
            return ResponseEntity.ok(savedPrices);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // POST get products with selling prices (for frontend integration)
    @PostMapping("/products")
    public ResponseEntity<List<Map<String, Object>>> getProductsWithSellingPrices(@RequestBody List<Map<String, Object>> products) {
        try {
            List<Map<String, Object>> productsWithPrices = sellingPriceService.getProductsWithSellingPrices(products);
            return ResponseEntity.ok(productsWithPrices);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // DELETE selling price by product ID
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteSellingPrice(@PathVariable Long productId) {
        try {
            sellingPriceService.deleteSellingPrice(productId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // GET selling prices map (productId -> sellingPrice)
    @GetMapping("/map")
    public ResponseEntity<Map<Long, Double>> getSellingPricesMap() {
        try {
            List<SellingPrice> allPrices = sellingPriceService.getAllSellingPrices();
            Map<Long, Double> priceMap = new HashMap<>();

            for (SellingPrice price : allPrices) {
                priceMap.put(price.getProductId(), price.getSellingPrice());
            }

            return ResponseEntity.ok(priceMap);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
