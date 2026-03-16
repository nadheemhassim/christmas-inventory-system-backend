package com.example.inventory.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "selling_prices")
public class SellingPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", unique = true)
    private Long productId;

    @Column(name = "selling_price")
    private Double sellingPrice;

    // Default constructor
    public SellingPrice() {}

    // Constructor with parameters
    public SellingPrice(Long productId, Double sellingPrice) {
        this.productId = productId;
        this.sellingPrice = sellingPrice;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
