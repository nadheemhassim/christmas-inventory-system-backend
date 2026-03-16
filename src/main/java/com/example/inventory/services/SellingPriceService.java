package com.example.inventory.services;

import com.example.inventory.entity.SellingPrice;
import com.example.inventory.repository.SellingPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SellingPriceService {

    @Autowired
    private SellingPriceRepository sellingPriceRepository;

    // Get all selling prices
    public List<SellingPrice> getAllSellingPrices() {
        return sellingPriceRepository.findAll();
    }

    // Get selling price by product ID
    public SellingPrice getSellingPriceByProductId(Long productId) {
        return sellingPriceRepository.findByProductId(productId)
                .orElse(null);
    }

    // Save or update selling price
    public SellingPrice saveSellingPrice(Long productId, Double sellingPrice) {
        Optional<SellingPrice> existingPrice = sellingPriceRepository.findByProductId(productId);

        if (existingPrice.isPresent()) {
            // Update existing
            SellingPrice price = existingPrice.get();
            price.setSellingPrice(sellingPrice);
            return sellingPriceRepository.save(price);
        } else {
            // Create new
            SellingPrice newPrice = new SellingPrice(productId, sellingPrice);
            return sellingPriceRepository.save(newPrice);
        }
    }

    // Bulk save or update selling prices
    public List<SellingPrice> bulkUpdateSellingPrices(List<Map<String, Object>> priceRequests) {
        List<SellingPrice> savedPrices = new ArrayList<>();

        for (Map<String, Object> request : priceRequests) {
            Long productId = ((Number) request.get("productId")).longValue();
            Double sellingPrice = ((Number) request.get("sellingPrice")).doubleValue();

            SellingPrice savedPrice = saveSellingPrice(productId, sellingPrice);
            savedPrices.add(savedPrice);
        }

        return savedPrices;
    }

    // Get selling prices for multiple products
    public Map<Long, Double> getSellingPricesForProducts(List<Long> productIds) {
        List<SellingPrice> prices = sellingPriceRepository.findByProductIdIn(productIds);
        return prices.stream()
                .collect(Collectors.toMap(
                        SellingPrice::getProductId,
                        SellingPrice::getSellingPrice
                ));
    }

    // Delete selling price by product ID
    public void deleteSellingPrice(Long productId) {
        sellingPriceRepository.deleteByProductId(productId);
    }

    // Get products with selling prices
    public List<Map<String, Object>> getProductsWithSellingPrices(List<Map<String, Object>> products) {
        List<Long> productIds = products.stream()
                .map(product -> ((Number) product.get("id")).longValue())
                .collect(Collectors.toList());

        Map<Long, Double> sellingPrices = getSellingPricesForProducts(productIds);

        return products.stream()
                .map(product -> {
                    Long productId = ((Number) product.get("id")).longValue();
                    Double sellingPrice = sellingPrices.get(productId);

                    Map<String, Object> productWithPrice = new HashMap<>(product);
                    productWithPrice.put("sellingPrice", sellingPrice != null ? sellingPrice : product.get("unitPrice"));
                    return productWithPrice;
                })
                .collect(Collectors.toList());
    }
}
