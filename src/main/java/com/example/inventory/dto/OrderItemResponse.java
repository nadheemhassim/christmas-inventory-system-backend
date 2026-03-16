package com.example.inventory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    private double dutyAmount;
    private double clearingAmount;
    private double labourChargeAmount;
    private double bankChargeAmount;

    private double marketPrice;


    // 🔹 New fields for stock breakdown
    private int cartonsTaken;
    private int boxesTaken;
    private int productsTaken;
}
