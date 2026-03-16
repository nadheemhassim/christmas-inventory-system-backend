package com.example.inventory.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String itemNo;
    private String photo;
    private String description;
    private double length;
    private double width;
    private double height;
    private double volume;
    private int orderQuantity; // 🔹 New field for available quantity
    private int inventoryQuantity;

    @Column(name = "sold_quantity")
    private Integer soldQuantity = 0;

    @Column(name = "reserved_quantity")
    private Integer reservedQuantity = 0;



    // Packaging structure
    private int cartons;          // Number of cartons
    private int boxesPerCarton;   // Boxes inside each carton
    private int qtyPerBox;        // Products per box

    private int totalProducts;

    private Double unitPrice;// Auto-calculate

    // New percentage fields
    private double clearing;
    private double duty;
    private double labourCharge;
    private double bankCharge;
    private double discountCharge;
    // private double discount;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    @JsonIgnore
    private Invoice invoice;

    // 🔹 Link to Warehouse
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    @JsonIgnore
    private Warehouse warehouse;


    // Auto-calculate total products before saving/updating
    @PrePersist
    @PreUpdate
    public void calculateTotalProducts() {
        this.inventoryQuantity = this.qtyPerBox;
        this.totalProducts = this.cartons * this.boxesPerCarton * this.qtyPerBox;
    }
}
