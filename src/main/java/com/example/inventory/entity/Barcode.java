package com.example.inventory.entity;


import jakarta.persistence.*;
import jakarta.persistence.Id;

@Entity
@Table(name = "barcodes")
public class Barcode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "barcode", unique = true, nullable = false)
    private String barcode;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    // Constructors
    public Barcode() {}

    public Barcode(String barcode, Product product, Boolean isPrimary) {
        this.barcode = barcode;
        this.product = product;
        this.isPrimary = isPrimary;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
}
