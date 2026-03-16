package com.example.inventory.controllers;

import com.example.inventory.dto.OrderRequest;
import com.example.inventory.entity.*;
import com.example.inventory.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {



    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody OrderRequest orderRequest) {
        try {
            Order updatedOrder = orderService.updateOrder(id, orderRequest);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    // Product search endpoints
    @GetMapping("/products/search")
    public List<Product> searchProducts(@RequestParam String searchTerm) {
        return orderService.searchProducts(searchTerm);
    }

    @GetMapping("/products/itemno/{itemNo}")
    public ResponseEntity<Product> getProductByItemNo(@PathVariable String itemNo) {
        Product product = orderService.findProductByItemNo(itemNo);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    // Barcode endpoints
    @PostMapping("/barcode/scan")
    public ResponseEntity<Product> scanBarcode(@RequestBody BarcodeScanRequest request) {
        Product product = orderService.smartBarcodeSearch(request.getBarcode());
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{orderId}/barcode/add-item")
    public ResponseEntity<?> addItemWithBarcode(
            @PathVariable Long orderId,
            @RequestBody QuickAddRequest request) {

        OrderItem item = orderService.addItemWithBarcode(
                orderId,
                request.getBarcode(),
                request.getQuantity(),
                request.getUnitPrice()
        );

        return item != null ? ResponseEntity.ok(item) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/{orderId}/barcode/quick-add")
    public ResponseEntity<?> quickAddWithBarcode(
            @PathVariable Long orderId,
            @RequestBody QuickAddRequest request) {

        OrderItem item = orderService.quickAddOrderItem(orderId, request.getBarcode(), request.getQuantity());
        return item != null ? ResponseEntity.ok(item) : ResponseEntity.badRequest().build();
    }

    // Order item management
    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<?> removeOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {

        boolean removed = orderService.removeOrderItem(orderId, itemId);
        return removed ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{orderId}/items/{itemId}/quantity")
    public ResponseEntity<OrderItem> updateOrderItemQuantity(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {

        OrderItem item = orderService.updateOrderItemQuantity(orderId, itemId, quantity);
        return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
    }

    // Dropdown data endpoints
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return orderService.getAllCustomers();
    }

    @GetMapping("/salespersons")
    public List<Salesperson> getAllSalespersons() {
        return orderService.getAllSalespersons();
    }

    @GetMapping("/customers/search")
    public List<Customer> searchCustomers(@RequestParam String name) {
        return orderService.searchCustomers(name);
    }

    @GetMapping("/salespersons/search")
    public List<Salesperson> searchSalespersons(@RequestParam String name) {
        return orderService.searchSalespersons(name);
    }

    // 🔹 UPDATE ORDER DISCOUNT AND DELIVERY CHARGE
    @PutMapping("/{orderId}/discount")
    public ResponseEntity<Order> updateOrderDiscount(
            @PathVariable Long orderId,
            @RequestBody OrderDiscountUpdateRequest request) {

        try {
            Order updatedOrder = orderService.updateOrderDiscount(
                    orderId,
                    request.getOrderDiscount(),
                    request.getDeliveryCharge()
            );
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Embedded DTO classes to resolve the compilation errors
    public static class BarcodeScanRequest {
        private String barcode;

        public BarcodeScanRequest() {}

        public BarcodeScanRequest(String barcode) {
            this.barcode = barcode;
        }

        public String getBarcode() { return barcode; }
        public void setBarcode(String barcode) { this.barcode = barcode; }
    }

    public static class QuickAddRequest {
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

    // DTO for discount update request
    public static class OrderDiscountUpdateRequest {
        private BigDecimal orderDiscount;
        private BigDecimal deliveryCharge;

        public OrderDiscountUpdateRequest() {}

        public OrderDiscountUpdateRequest(BigDecimal orderDiscount, BigDecimal deliveryCharge) {
            this.orderDiscount = orderDiscount;
            this.deliveryCharge = deliveryCharge;
        }

        public BigDecimal getOrderDiscount() { return orderDiscount; }
        public void setOrderDiscount(BigDecimal orderDiscount) { this.orderDiscount = orderDiscount; }

        public BigDecimal getDeliveryCharge() { return deliveryCharge; }
        public void setDeliveryCharge(BigDecimal deliveryCharge) { this.deliveryCharge = deliveryCharge; }
    }
}



