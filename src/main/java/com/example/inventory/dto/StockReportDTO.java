package com.example.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockReportDTO {
    private Long productId;
    private String itemNo;
    private String description;
    private String warehouseLocation;
    private int totalProducts;
    private int availableQuantity;
    private int reservedQuantity;
    private int cartons;
    private int boxesPerCarton;
    private int qtyPerBox;
    private Double unitPrice;
    private double volume;

    // Add this constructor that matches your JPQL query exactly
    public StockReportDTO(Long productId, String itemNo, String description, String warehouseLocation,
                          int totalProducts, int availableQuantity, int reservedQuantity,
                          int cartons, int boxesPerCarton, int qtyPerBox, Double unitPrice, double volume) {
        this.productId = productId;
        this.itemNo = itemNo;
        this.description = description;
        this.warehouseLocation = warehouseLocation;
        this.totalProducts = totalProducts;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
        this.cartons = cartons;
        this.boxesPerCarton = boxesPerCarton;
        this.qtyPerBox = qtyPerBox;
        this.unitPrice = unitPrice;
        this.volume = volume;
    }

    // Additional fields that might be needed
    private String photoUrl;
    private String warehouseManager;
    private String warehouseContact;
    private double length;
    private double width;
    private double height;
    private double clearing;
    private double duty;
    private double labourCharge;
    private double bankCharge;
    private double discountCharge;
}