package com.example.inventory.dto;

import com.example.inventory.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private Long customerId;
    private Long salespersonId;
    private BigDecimal orderDiscount;
    private BigDecimal deliveryCharge;
    private OrderStatus status; // 🔹 NEW FIELD
    private List<OrderItemRequest> orderItems;

    // Constructors
    public OrderRequest() {}

    // Getters and Setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getSalespersonId() { return salespersonId; }
    public void setSalespersonId(Long salespersonId) { this.salespersonId = salespersonId; }

    public BigDecimal getOrderDiscount() { return orderDiscount; }
    public void setOrderDiscount(BigDecimal orderDiscount) { this.orderDiscount = orderDiscount; }

    public BigDecimal getDeliveryCharge() { return deliveryCharge; }
    public void setDeliveryCharge(BigDecimal deliveryCharge) { this.deliveryCharge = deliveryCharge; }

    public OrderStatus getStatus() { return status; } // 🔹 NEW
    public void setStatus(OrderStatus status) { this.status = status; } // 🔹 NEW

    public List<OrderItemRequest> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemRequest> orderItems) { this.orderItems = orderItems; }

}
