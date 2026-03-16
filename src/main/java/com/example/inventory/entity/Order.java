package com.example.inventory.entity;

import com.example.inventory.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders") // "order" is a reserved keyword in SQL
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "salesperson_id")
    private Salesperson salesperson;

    @Column(name = "order_discount")
    private BigDecimal orderDiscount;

    @Column(name = "delivery_charge")
    private BigDecimal deliveryCharge;

    @Column(name = "total_market_price")
    private BigDecimal totalMarketPrice;

    @Column(name = "final_total")
    private BigDecimal finalTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status = OrderStatus.PENDING;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();

    // Constructors
//    public Order() {
//        this.orderDate = LocalDateTime.now();
//    }

    public Order() {
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }


    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Salesperson getSalesperson() { return salesperson; }
    public void setSalesperson(Salesperson salesperson) { this.salesperson = salesperson; }

    public BigDecimal getOrderDiscount() { return orderDiscount; }
    public void setOrderDiscount(BigDecimal orderDiscount) { this.orderDiscount = orderDiscount; }

    public BigDecimal getDeliveryCharge() { return deliveryCharge; }
    public void setDeliveryCharge(BigDecimal deliveryCharge) { this.deliveryCharge = deliveryCharge; }

    public BigDecimal getTotalMarketPrice() { return totalMarketPrice; }
    public void setTotalMarketPrice(BigDecimal totalMarketPrice) { this.totalMarketPrice = totalMarketPrice; }

    public BigDecimal getFinalTotal() { return finalTotal; }
    public void setFinalTotal(BigDecimal finalTotal) { this.finalTotal = finalTotal; }

    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }

    // Helper methods
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
    }

    public void calculateTotals() {
        BigDecimal itemsTotal = BigDecimal.ZERO;

        for (OrderItem item : orderItems) {
            item.calculateTotal();
            itemsTotal = itemsTotal.add(item.getTotalPrice());
        }

        this.totalMarketPrice = itemsTotal;

        BigDecimal discount = this.orderDiscount != null ? this.orderDiscount : BigDecimal.ZERO;
        BigDecimal delivery = this.deliveryCharge != null ? this.deliveryCharge : BigDecimal.ZERO;

        this.finalTotal = itemsTotal.subtract(discount).add(delivery);
    }
}