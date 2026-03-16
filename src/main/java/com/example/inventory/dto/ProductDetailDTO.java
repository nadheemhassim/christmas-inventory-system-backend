package com.example.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {
    private Long productId;
    private String itemNo;
    private String description;
    private String photo;
    private double length;
    private double width;
    private double height;
    private double volume;
    private int totalProducts;
    private int availableQuantity;
    private int reservedQuantity;
    private int cartons;
    private int boxesPerCarton;
    private int qtyPerBox;
    private Double unitPrice;
    private String warehouseLocation;
    private String warehouseManager;
    private String warehouseContact;

    // Charges
    private double clearing;
    private double duty;
    private double labourCharge;
    private double bankCharge;
    private double discountCharge;

    // Packaging breakdown
    private int fullCartons;
    private int partialBoxes;
    private int individualProducts;
}
