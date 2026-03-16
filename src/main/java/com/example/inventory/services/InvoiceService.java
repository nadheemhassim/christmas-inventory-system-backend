package com.example.inventory.services;

import com.example.inventory.entity.Invoice;
import com.example.inventory.entity.Supplier;
import com.example.inventory.repository.InvoiceRepository;
import com.example.inventory.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final SupplierRepository supplierRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          SupplierRepository supplierRepository) {
        this.invoiceRepository = invoiceRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public Invoice createInvoice(Long supplierId, Invoice invoice) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        invoice.setSupplier(supplier);
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAllByOrderByInvoiceDateDesc();
    }
}
