package com.example.inventory.dto;

import com.example.inventory.entity.Product;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class ProductDTO {
    private Long id;
    private String itemNo;
    private String description;
    private String photoUrl;
    private double length;
    private double width;
    private double height;
    private double volume;

    private int cartons;
    private int boxesPerCarton;
    private int qtyPerBox;
    private int totalProducts;
    private double totalVolume;

    private double clearing;
    private double duty;
    private double labourCharge;
    private double bankCharge;
    private Double unitPrice;
    private int inventoryQuantity;


    public ProductDTO(Product product) {
        this.id = product.getId();
        this.itemNo = product.getItemNo();
        this.description = product.getDescription();
        this.length = product.getLength();
        this.width = product.getWidth();
        this.height = product.getHeight();
        this.volume = product.getVolume();

        this.cartons = product.getCartons();
        this.boxesPerCarton = product.getBoxesPerCarton();
        this.qtyPerBox = product.getQtyPerBox();
        this.totalProducts = product.getTotalProducts();
        this.unitPrice=product.getUnitPrice();
        this.inventoryQuantity=product.getInventoryQuantity();

        this.totalVolume = this.length * this.width * this.height;

        if (product.getPhoto() != null) {
//            this.photoUrl = "http://192.168.1.201:8081/uploads/" + product.getPhoto();
            // Dynamic URL based on the current request
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            this.photoUrl = baseUrl + "/uploads/" + product.getPhoto();
        } else {
            this.photoUrl = null;
        }

        this.clearing = product.getClearing();
        this.duty = product.getDuty();
        this.labourCharge = product.getLabourCharge();
        this.bankCharge = product.getBankCharge();
    }

    // Getters and setters for all fields

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getItemNo() { return itemNo; }
    public void setItemNo(String itemNo) { this.itemNo = itemNo; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getVolume() { return volume; }
    public void setVolume(double volume) { this.volume = volume; }

    public double getTotalVolume() { return totalVolume; }
    public void setTotalVolume(double totalVolume) { this.totalVolume = totalVolume; }

    public double getClearing() { return clearing; }
    public void setClearing(double clearing) { this.clearing = clearing; }

    public double getDuty() { return duty; }
    public void setDuty(double duty) { this.duty = duty; }

    public double getLabourCharge() { return labourCharge; }
    public void setLabourCharge(double labourCharge) { this.labourCharge = labourCharge; }

    public double getBankCharge() { return bankCharge; }
    public void setBankCharge(double bankCharge) { this.bankCharge = bankCharge; }

    public int getCartons() {
        return cartons;
    }

    public void setCartons(int cartons) {
        this.cartons = cartons;
    }

    public int getBoxesPerCarton() {
        return boxesPerCarton;
    }

    public void setBoxesPerCarton(int boxesPerCarton) {
        this.boxesPerCarton = boxesPerCarton;
    }

    public int getQtyPerBox() {
        return qtyPerBox;
    }

    public void setQtyPerBox(int qtyPerBox) {
        this.qtyPerBox = qtyPerBox;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(int inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }
}
