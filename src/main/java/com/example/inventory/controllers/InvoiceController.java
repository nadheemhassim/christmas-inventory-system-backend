package com.example.inventory.controllers;

import com.example.inventory.entity.Invoice;
import com.example.inventory.services.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/supplier/{supplierId}")
    public ResponseEntity<Invoice> createInvoice(
            @PathVariable Long supplierId,
            @RequestBody Invoice invoice) {
        Invoice createdInvoice = invoiceService.createInvoice(supplierId, invoice);
        return ResponseEntity.ok(createdInvoice);
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }

}
