package com.example.inventory.services;

import com.example.inventory.dto.OrderItemRequest;
import com.example.inventory.dto.OrderRequest;
import com.example.inventory.entity.*;
import com.example.inventory.enums.OrderStatus;
import com.example.inventory.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SalespersonRepository salespersonRepository;

    @Autowired
    private BarcodeRepository barcodeRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

//    public Order createOrder(OrderRequest orderRequest) {
//        Order order = new Order();
//
//        // Set basic order information
//        if (orderRequest.getCustomerId() != null) {
//            Customer customer = customerRepository.findById(orderRequest.getCustomerId())
//                    .orElseThrow(() -> new RuntimeException("Customer not found"));
//            order.setCustomer(customer);
//        }
//
//        if (orderRequest.getSalespersonId() != null) {
//            Salesperson salesperson = salespersonRepository.findById(orderRequest.getSalespersonId())
//                    .orElseThrow(() -> new RuntimeException("Salesperson not found"));
//            order.setSalesperson(salesperson);
//        }
//
//        order.setOrderDiscount(orderRequest.getOrderDiscount());
//        order.setDeliveryCharge(orderRequest.getDeliveryCharge());
//
//        // 🔹 SET ORDER STATUS
//        if (orderRequest.getStatus() != null) {
//            order.setStatus(orderRequest.getStatus());
//        } else {
//            order.setStatus(OrderStatus.PENDING);
//        }
//
//        // 🔹 VALIDATE INVENTORY BEFORE CREATING ORDER
//        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
//            Product product = productRepository.findById(itemRequest.getProductId())
//                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemRequest.getProductId()));
//
//            // Check if enough inventory is available
//            if (product.getInventoryQuantity() < itemRequest.getQuantity()) {
//                throw new RuntimeException("Insufficient inventory for product: " + product.getItemNo() +
//                        ". Available: " + product.getInventoryQuantity() +
//                        ", Requested: " + itemRequest.getQuantity());
//            }
//        }
//
//        // Add order items and REDUCE INVENTORY
//        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
//            Product product = productRepository.findById(itemRequest.getProductId())
//                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemRequest.getProductId()));
//
//            // 🔹 REDUCE INVENTORY QUANTITY
//            int newInventoryQuantity = product.getInventoryQuantity() - itemRequest.getQuantity();
//            product.setInventoryQuantity(newInventoryQuantity);
//            productRepository.save(product);
//
//            OrderItem orderItem = new OrderItem();
//            orderItem.setProduct(product);
//            orderItem.setQuantity(itemRequest.getQuantity());
//
//            // Set unit price
//            orderItem.setUnitPrice(itemRequest.getUnitPrice() != null ?
//                    itemRequest.getUnitPrice() : BigDecimal.valueOf(product.getUnitPrice()));
//            orderItem.calculateTotal();
//
//            order.addOrderItem(orderItem);
//        }
//
//        order.calculateTotals();
//        return orderRepository.save(order);
//    }

    public Order createOrder(OrderRequest orderRequest) {
        Order order = new Order();

        // Set basic order information
        if (orderRequest.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            order.setCustomer(customer);
        }

        if (orderRequest.getSalespersonId() != null) {
            Salesperson salesperson = salespersonRepository.findById(orderRequest.getSalespersonId())
                    .orElseThrow(() -> new RuntimeException("Salesperson not found"));
            order.setSalesperson(salesperson);
        }

        order.setOrderDiscount(orderRequest.getOrderDiscount());
        order.setDeliveryCharge(orderRequest.getDeliveryCharge());

        // 🔹 SET ORDER STATUS
        if (orderRequest.getStatus() != null) {
            order.setStatus(orderRequest.getStatus());
        } else {
            order.setStatus(OrderStatus.PENDING);
        }

        // 🔹 DEBUG: Check initial inventory
        System.out.println("=== INITIAL INVENTORY CHECK ===");
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId()).get();
            System.out.println("Product ID: " + product.getId() + ", ItemNo: " + product.getItemNo() +
                    ", Initial Inventory: " + product.getInventoryQuantity());
        }

        // 🔹 VALIDATE INVENTORY BEFORE CREATING ORDER
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemRequest.getProductId()));

            // Check if enough inventory is available
            if (product.getInventoryQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient inventory for product: " + product.getItemNo() +
                        ". Available: " + product.getInventoryQuantity() +
                        ", Requested: " + itemRequest.getQuantity());
            }
        }

        // Add order items and REDUCE INVENTORY
//        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
//            Product product = productRepository.findById(itemRequest.getProductId())
//                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemRequest.getProductId()));
//
//            // 🔹 DEBUG: Before reduction
//            System.out.println("BEFORE REDUCTION - Product: " + product.getItemNo() +
//                    ", Inventory: " + product.getInventoryQuantity() +
//                    ", Order Quantity: " + itemRequest.getQuantity());
//
//            // 🔹 REDUCE INVENTORY QUANTITY
//            int newInventoryQuantity = product.getInventoryQuantity() - itemRequest.getQuantity();
//            product.setInventoryQuantity(newInventoryQuantity);
//
//            // 🔹 DEBUG: After calculation
//            System.out.println("AFTER CALCULATION - New Inventory: " + newInventoryQuantity);
//
//            // Save the product
//            Product savedProduct = productRepository.save(product);
//
//            // 🔹 DEBUG: After save
//            System.out.println("AFTER SAVE - Product: " + savedProduct.getItemNo() +
//                    ", Inventory: " + savedProduct.getInventoryQuantity());
//
//            OrderItem orderItem = new OrderItem();
//            orderItem.setProduct(product);
//            orderItem.setQuantity(itemRequest.getQuantity());
//            orderItem.setUnitPrice(itemRequest.getUnitPrice() != null ?
//                    itemRequest.getUnitPrice() : BigDecimal.valueOf(product.getUnitPrice()));
//            orderItem.calculateTotal();
//
//            order.addOrderItem(orderItem);
//        }

        // Add order items and REDUCE INVENTORY
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemRequest.getProductId()));

            // 🔹 DEBUG: Before reduction
            System.out.println("BEFORE REDUCTION - Product: " + product.getItemNo() +
                    ", Inventory: " + product.getInventoryQuantity() +
                    ", Order Quantity: " + itemRequest.getQuantity());

            // 🔹 DIRECT SQL UPDATE - NO ENTITY SAVING!
            productRepository.reduceInventory(product.getId(), itemRequest.getQuantity());
            System.out.println("SQL UPDATE EXECUTED - Reduced inventory by " + itemRequest.getQuantity());

            // 🔹 GET FRESH INVENTORY COUNT
            Integer currentInventory = productRepository.getInventoryQuantity(product.getId());
            System.out.println("CURRENT INVENTORY AFTER SQL: " + currentInventory);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(itemRequest.getUnitPrice() != null ?
                    itemRequest.getUnitPrice() : BigDecimal.valueOf(product.getUnitPrice()));
            orderItem.calculateTotal();

            order.addOrderItem(orderItem);
        }

        order.calculateTotals();
        Order savedOrder = orderRepository.save(order);

        // 🔹 DEBUG: Final check
        System.out.println("=== FINAL INVENTORY CHECK ===");
        for (OrderItem item : savedOrder.getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getId()).get();
            System.out.println("Product ID: " + product.getId() + ", Final Inventory: " + product.getInventoryQuantity());
        }

        return savedOrder;
    }

    public Order updateOrder(Long id, OrderRequest orderRequest) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            // Update customer
            if (orderRequest.getCustomerId() != null) {
                Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                        .orElseThrow(() -> new RuntimeException("Customer not found"));
                order.setCustomer(customer);
            } else {
                order.setCustomer(null);
            }

            // Update salesperson
            if (orderRequest.getSalespersonId() != null) {
                Salesperson salesperson = salespersonRepository.findById(orderRequest.getSalespersonId())
                        .orElseThrow(() -> new RuntimeException("Salesperson not found"));
                order.setSalesperson(salesperson);
            } else {
                order.setSalesperson(null);
            }

            order.setOrderDiscount(orderRequest.getOrderDiscount());
            order.setDeliveryCharge(orderRequest.getDeliveryCharge());

            // 🔹 UPDATE ORDER STATUS
            if (orderRequest.getStatus() != null) {
                order.setStatus(orderRequest.getStatus());
            }

            // 🔹 COMPLEX INVENTORY MANAGEMENT FOR ORDER UPDATE
            // Store old items for inventory restoration
            List<OrderItem> oldItems = new ArrayList<>(order.getOrderItems());

            // Validate new inventory requirements first
//            for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
//                Product product = productRepository.findById(itemRequest.getProductId())
//                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemRequest.getProductId()));
//
//                // Calculate available inventory considering we'll restore old quantities
//                int currentlyAvailable = product.getInventoryQuantity();
//
//                // Find if this product was in old order and how much quantity we'll get back
//                int restoredQuantity = oldItems.stream()
//                        .filter(oldItem -> oldItem.getProduct().getId().equals(product.getId()))
//                        .mapToInt(OrderItem::getQuantity)
//                        .sum();
//
//                int effectivelyAvailable = currentlyAvailable + restoredQuantity;
//
//                if (effectivelyAvailable < itemRequest.getQuantity()) {
//                    throw new RuntimeException("Insufficient inventory for product: " + product.getItemNo() +
//                            ". Effectively available: " + effectivelyAvailable +
//                            ", Requested: " + itemRequest.getQuantity());
//                }
//            }

            // 🔹 RESTORE INVENTORY FROM OLD ORDER ITEMS

            // Add order items and REDUCE INVENTORY
            for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemRequest.getProductId()));

                // 🔹 DEBUG: Before reduction
                System.out.println("BEFORE REDUCTION - Product: " + product.getItemNo() +
                        ", Inventory: " + product.getInventoryQuantity() +
                        ", Order Quantity: " + itemRequest.getQuantity());

                // 🔹 DIRECT SQL UPDATE - NO ENTITY SAVING!
                productRepository.reduceInventory(product.getId(), itemRequest.getQuantity());
                System.out.println("SQL UPDATE EXECUTED - Reduced inventory by " + itemRequest.getQuantity());

                // 🔹 GET FRESH INVENTORY COUNT
                Integer currentInventory = productRepository.getInventoryQuantity(product.getId());
                System.out.println("CURRENT INVENTORY AFTER SQL: " + currentInventory);

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(itemRequest.getQuantity());
                orderItem.setUnitPrice(itemRequest.getUnitPrice() != null ?
                        itemRequest.getUnitPrice() : BigDecimal.valueOf(product.getUnitPrice()));
                orderItem.calculateTotal();

                order.addOrderItem(orderItem);
            }

            for (OrderItem oldItem : oldItems) {
                Product oldProduct = oldItem.getProduct();
                int restoredQuantity = oldProduct.getInventoryQuantity() + oldItem.getQuantity();
                oldProduct.setInventoryQuantity(restoredQuantity);
                productRepository.save(oldProduct);
            }

            // Clear existing items and add new ones with updated inventory
            order.getOrderItems().clear();
            for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemRequest.getProductId()));

                // 🔹 DEDUCT NEW QUANTITIES FROM INVENTORY
                int newInventoryQuantity = product.getInventoryQuantity() - itemRequest.getQuantity();
                product.setInventoryQuantity(newInventoryQuantity);
                productRepository.save(product);

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(itemRequest.getQuantity());
                orderItem.setUnitPrice(itemRequest.getUnitPrice() != null ?
                        itemRequest.getUnitPrice() : BigDecimal.valueOf(product.getUnitPrice()));
                orderItem.calculateTotal();

                order.addOrderItem(orderItem);
            }

            order.calculateTotals();
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found with id: " + id);
    }

    public void deleteOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            // 🔹 RESTORE INVENTORY WHEN ORDER IS DELETED
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                int restoredQuantity = product.getInventoryQuantity() + item.getQuantity();
                product.setInventoryQuantity(restoredQuantity);
                productRepository.save(product);
            }

            orderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Order not found with id: " + id);
        }
    }

    // 🔹 UPDATE ORDER DISCOUNT AND DELIVERY CHARGE
    public Order updateOrderDiscount(Long orderId, BigDecimal orderDiscount, BigDecimal deliveryCharge) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            // Update discount and delivery charge
            order.setOrderDiscount(orderDiscount != null ? orderDiscount : BigDecimal.ZERO);
            order.setDeliveryCharge(deliveryCharge != null ? deliveryCharge : BigDecimal.ZERO);

            // Recalculate totals
            order.calculateTotals();

            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found with id: " + orderId);
    }

    // 🔹 NEW: Update order status with inventory handling for cancellations
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderStatus oldStatus = order.getStatus();

            // Handle inventory restoration if order is being cancelled
            if (newStatus == OrderStatus.CANCELLED && oldStatus != OrderStatus.CANCELLED) {
                for (OrderItem item : order.getOrderItems()) {
                    Product product = item.getProduct();
                    int restoredQuantity = product.getInventoryQuantity() + item.getQuantity();
                    product.setInventoryQuantity(restoredQuantity);
                    productRepository.save(product);
                }
            }
            // Handle inventory deduction if order is being confirmed from cancelled state
            else if (oldStatus == OrderStatus.CANCELLED &&
                    (newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.PROCESSING)) {
                for (OrderItem item : order.getOrderItems()) {
                    Product product = item.getProduct();
                    if (product.getInventoryQuantity() < item.getQuantity()) {
                        throw new RuntimeException("Insufficient inventory to reactivate order for product: " +
                                product.getItemNo());
                    }
                    int newInventoryQuantity = product.getInventoryQuantity() - item.getQuantity();
                    product.setInventoryQuantity(newInventoryQuantity);
                    productRepository.save(product);
                }
            }

            order.setStatus(newStatus);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found with id: " + orderId);
    }

    public Product findProductByItemNo(String itemNo) {
        List<Product> products = productRepository.findByItemNoContainingIgnoreCase(itemNo);
        return products.isEmpty() ? null : products.get(0);
    }

    public List<Product> searchProducts(String searchTerm) {
        return productRepository.searchByItemNoOrDescription(searchTerm);
    }

    public List<Product> searchAvailableProducts(String searchTerm) {
        return productRepository.searchAvailableProducts(searchTerm);
    }

    // BARCODE FUNCTIONALITY

    /**
     * Find product by scanning barcode (exact match)
     */
    public Product findProductByBarcode(String barcode) {
        // First try to find in barcode table
        Optional<Barcode> barcodeOpt = barcodeRepository.findByBarcode(barcode);
        if (barcodeOpt.isPresent()) {
            return barcodeOpt.get().getProduct();
        }

        // If no separate barcode table, treat the barcode as item number
        return productRepository.findByItemNo(barcode).orElse(null);
    }

    /**
     * Find product by item number (exact match for barcode scanning)
     */
    public Product findProductByItemNoExact(String itemNo) {
        return productRepository.findByItemNo(itemNo).orElse(null);
    }

    /**
     * Smart barcode search - tries multiple approaches
     */
    public Product smartBarcodeSearch(String scannedCode) {
        // 1. Try exact match in barcode table
        Optional<Barcode> barcodeOpt = barcodeRepository.findByBarcode(scannedCode);
        if (barcodeOpt.isPresent()) {
            return barcodeOpt.get().getProduct();
        }

        // 2. Try exact match with item number
        Optional<Product> productOpt = productRepository.findByItemNo(scannedCode);
        if (productOpt.isPresent()) {
            return productOpt.get();
        }

        // 3. Try prefix matching (common in barcode systems)
        List<Product> prefixMatches = productRepository.findByItemNoStartingWith(scannedCode);
        if (prefixMatches.size() == 1) {
            return prefixMatches.get(0);
        }

        // 4. Try suffix matching
        List<Product> suffixMatches = productRepository.findByItemNoEndingWith(scannedCode);
        if (suffixMatches.size() == 1) {
            return suffixMatches.get(0);
        }

        return null;
    }

    /**
     * Quick add item to order using barcode
     */
    public OrderItem quickAddOrderItem(Long orderId, String barcode, Integer quantity) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Product product = smartBarcodeSearch(barcode);
            if (product != null) {
                // 🔹 CHECK INVENTORY BEFORE ADDING
                if (product.getInventoryQuantity() < quantity) {
                    throw new RuntimeException("Insufficient inventory for product: " + product.getItemNo());
                }

                Order order = orderOpt.get();

                // Check if item already exists in order
                Optional<OrderItem> existingItem = order.getOrderItems().stream()
                        .filter(item -> item.getProduct().getId().equals(product.getId()))
                        .findFirst();

                if (existingItem.isPresent()) {
                    // Update quantity of existing item
                    OrderItem item = existingItem.get();
                    int newQuantity = item.getQuantity() + quantity;

                    // Check inventory for updated quantity
                    if (product.getInventoryQuantity() < newQuantity) {
                        throw new RuntimeException("Insufficient inventory for updated quantity");
                    }

                    item.setQuantity(newQuantity);
                    order.calculateTotals();
                    orderRepository.save(order);
                    return item;
                } else {
                    // Create new order item
                    OrderItem newItem = new OrderItem();
                    newItem.setProduct(product);
                    newItem.setQuantity(quantity);
                    newItem.setUnitPrice(BigDecimal.valueOf(product.getUnitPrice()));
                    newItem.calculateTotal();

                    order.addOrderItem(newItem);
                    order.calculateTotals();
                    orderRepository.save(order);
                    return newItem;
                }
            }
        }
        return null;
    }

    /**
     * Add item to order with barcode scan and custom price
     */
    public OrderItem addItemWithBarcode(Long orderId, String barcode, Integer quantity, BigDecimal unitPrice) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Product product = smartBarcodeSearch(barcode);
            if (product != null) {
                // 🔹 CHECK INVENTORY
                if (product.getInventoryQuantity() < quantity) {
                    throw new RuntimeException("Insufficient inventory for product: " + product.getItemNo());
                }

                Order order = orderOpt.get();

                OrderItem newItem = new OrderItem();
                newItem.setProduct(product);
                newItem.setQuantity(quantity);
                newItem.setUnitPrice(unitPrice != null ? unitPrice : BigDecimal.valueOf(product.getUnitPrice()));
                newItem.calculateTotal();

                order.addOrderItem(newItem);
                order.calculateTotals();
                orderRepository.save(order);
                return newItem;
            }
        }
        return null;
    }

    /**
     * Remove item from order
     */
    public boolean removeOrderItem(Long orderId, Long itemId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            Optional<OrderItem> itemToRemove = order.getOrderItems().stream()
                    .filter(item -> item.getId().equals(itemId))
                    .findFirst();

            if (itemToRemove.isPresent()) {
                // 🔹 RESTORE INVENTORY WHEN ITEM IS REMOVED
                Product product = itemToRemove.get().getProduct();
                int restoredQuantity = product.getInventoryQuantity() + itemToRemove.get().getQuantity();
                product.setInventoryQuantity(restoredQuantity);
                productRepository.save(product);

                boolean removed = order.getOrderItems().removeIf(item -> item.getId().equals(itemId));
                if (removed) {
                    order.calculateTotals();
                    orderRepository.save(order);
                }
                return removed;
            }
        }
        return false;
    }

    /**
     * Update order item quantity
     */
    public OrderItem updateOrderItemQuantity(Long orderId, Long itemId, Integer quantity) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            Optional<OrderItem> itemOpt = order.getOrderItems().stream()
                    .filter(item -> item.getId().equals(itemId))
                    .findFirst();

            if (itemOpt.isPresent()) {
                OrderItem item = itemOpt.get();
                Product product = item.getProduct();

                // 🔹 CALCULATE INVENTORY CHANGE
                int quantityDifference = quantity - item.getQuantity();

                if (quantityDifference > 0) {
                    // Increasing quantity - check inventory
                    if (product.getInventoryQuantity() < quantityDifference) {
                        throw new RuntimeException("Insufficient inventory to increase quantity");
                    }
                    product.setInventoryQuantity(product.getInventoryQuantity() - quantityDifference);
                } else {
                    // Decreasing quantity - restore inventory
                    product.setInventoryQuantity(product.getInventoryQuantity() + Math.abs(quantityDifference));
                }
                productRepository.save(product);

                item.setQuantity(quantity);
                order.calculateTotals();
                orderRepository.save(order);
                return item;
            }
        }
        return null;
    }

    /**
     * Get all customers for dropdown
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Get all salespersons for dropdown
     */
    public List<Salesperson> getAllSalespersons() {
        return salespersonRepository.findAll();
    }

    /**
     * Search customers by name
     */
    public List<Customer> searchCustomers(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Search salespersons by name
     */
    public List<Salesperson> searchSalespersons(String name) {
        return salespersonRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Validate inventory before order creation
     */
    public boolean validateInventory(OrderRequest orderRequest) {
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getInventoryQuantity() < itemRequest.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get available inventory for a product
     */
    public Integer getAvailableInventory(Long productId) {
        return productRepository.getInventoryQuantity(productId);
    }

    /**
     * Get orders by status
     */
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * Get orders for a specific product
     */
    public List<Order> getOrdersByProductId(Long productId) {
        return orderRepository.findOrdersByProductId(productId);
    }
}