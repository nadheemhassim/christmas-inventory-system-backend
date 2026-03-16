package com.example.inventory.dto;

import java.math.BigDecimal;

public class QuickAddRequest {
    private String barcode;
    private Integer quantity;
    private BigDecimal unitPrice;

    public QuickAddRequest() {}

    public QuickAddRequest(String barcode, Integer quantity, BigDecimal unitPrice) {
        this.barcode = barcode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}
