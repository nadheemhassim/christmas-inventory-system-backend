package com.example.inventory.dto;

public class BarcodeScanRequest {

    private String barcode;

    public BarcodeScanRequest() {}

    public BarcodeScanRequest(String barcode) {
        this.barcode = barcode;
    }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

}
