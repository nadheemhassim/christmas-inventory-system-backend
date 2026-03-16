package com.example.inventory.controllers;

import com.example.inventory.dto.ProductDTO;
import com.example.inventory.entity.Product;
import com.example.inventory.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/invoice/{invoiceId}", consumes = {"multipart/form-data"})
    public ResponseEntity<Product> createProduct(
            @PathVariable Long invoiceId,
            @RequestPart("product") Product product,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile) {
        Product createdProduct = productService.createProduct(invoiceId, product, photoFile);
        return ResponseEntity.ok(createdProduct);
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<ProductDTO>> getProductsByInvoice(@PathVariable Long invoiceId, HttpServletRequest request) {
        List<ProductDTO> dtoList = productService.getProductsByInvoice(invoiceId)
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> dtoList = productService.getAllProducts()
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") Product product,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile) {
        Product updatedProduct = productService.updateProduct(id, product, photoFile);
        return ResponseEntity.ok(updatedProduct);
    }
}
