package com.example.inventory.services;

import com.example.inventory.entity.Barcode;
import com.example.inventory.entity.Invoice;
import com.example.inventory.entity.Product;
import com.example.inventory.repository.BarcodeRepository;
import com.example.inventory.repository.InvoiceRepository;
import com.example.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;
    private final BarcodeRepository barcodeRepository;
    private final String UPLOAD_DIR = "uploads/";

    public ProductService(ProductRepository productRepository,
                          InvoiceRepository invoiceRepository,
                          BarcodeRepository barcodeRepository) {
        this.productRepository = productRepository;
        this.invoiceRepository = invoiceRepository;
        this.barcodeRepository = barcodeRepository;
    }

    // Create a new product and calculate total products
    public Product createProduct(Long invoiceId, Product product, MultipartFile photoFile) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        try {
            // Save image if exists
            if (photoFile != null && !photoFile.isEmpty()) {
                String savedFileName = saveFile(photoFile);
                product.setPhoto(savedFileName);
            }

            product.setInvoice(invoice);
            return productRepository.save(product);

        } catch (IOException e) {
            throw new RuntimeException("Failed to process file upload", e);
        }
    }

    // Save file to local storage with proper stream handling
    private String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

        Path filePath = uploadPath.resolve(uniqueFileName);

        // Use try-with-resources to ensure stream is closed properly
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return uniqueFileName;
    }

    // Get products for a specific invoice
    public List<Product> getProductsByInvoice(Long invoiceId) {
        return productRepository.findByInvoiceId(invoiceId);
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Reduce stock hierarchically based on cartons, boxes per carton, and products per box
    public void reduceStock(Long productId, int orderedQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (orderedQuantity > product.getTotalProducts()) {
            throw new RuntimeException("Not enough stock available");
        }

        int remainingProducts = product.getTotalProducts() - orderedQuantity;

        int qtyPerBox = product.getQtyPerBox();
        int boxesPerCarton = product.getBoxesPerCarton();
        int productsPerCarton = boxesPerCarton * qtyPerBox;

        int remainingCartons = remainingProducts / productsPerCarton;
        int leftoverProducts = remainingProducts % productsPerCarton;

        int remainingBoxes = leftoverProducts / qtyPerBox;
        int productsInLastBox = leftoverProducts % qtyPerBox;

        product.setCartons(remainingCartons);
        product.setTotalProducts(remainingProducts);

        productRepository.save(product);
    }

    // ✅ Update an existing product with proper file handling
    public Product updateProduct(Long id, Product updatedProduct, MultipartFile photoFile) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        try {
            // Update photo if new file is provided
            if (photoFile != null && !photoFile.isEmpty()) {
                // Delete old photo if exists
                if (existingProduct.getPhoto() != null) {
                    deleteFile(existingProduct.getPhoto());
                }

                // Save new photo
                String savedFileName = saveFile(photoFile);
                existingProduct.setPhoto(savedFileName);
            }

            // Update other fields
            existingProduct.setItemNo(updatedProduct.getItemNo());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setLength(updatedProduct.getLength());
            existingProduct.setWidth(updatedProduct.getWidth());
            existingProduct.setHeight(updatedProduct.getHeight());
            existingProduct.setVolume(updatedProduct.getVolume());
            existingProduct.setOrderQuantity(updatedProduct.getOrderQuantity());
            existingProduct.setQtyPerBox(updatedProduct.getQtyPerBox());
            existingProduct.setBoxesPerCarton(updatedProduct.getBoxesPerCarton());
            existingProduct.setTotalProducts(updatedProduct.getTotalProducts());
            existingProduct.setCartons(updatedProduct.getCartons());
            existingProduct.setUnitPrice(updatedProduct.getUnitPrice());
            existingProduct.setClearing(updatedProduct.getClearing());
            existingProduct.setDuty(updatedProduct.getDuty());
            existingProduct.setLabourCharge(updatedProduct.getLabourCharge());
            existingProduct.setBankCharge(updatedProduct.getBankCharge());
            existingProduct.setDiscountCharge(updatedProduct.getDiscountCharge());
            existingProduct.setInventoryQuantity(updatedProduct.getInventoryQuantity());

            return productRepository.save(existingProduct);

        } catch (IOException e) {
            throw new RuntimeException("Failed to process file upload during update", e);
        }
    }

    // Delete file with proper error handling
    private void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log warning but don't throw exception for file deletion failures
            System.err.println("Warning: Could not delete file: " + fileName);
            // You can also use a proper logger here
            // logger.warn("Could not delete file: {}", fileName, e);
        }
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
     * Search products by item number or description
     */
    public List<Product> searchProducts(String searchTerm) {
        return productRepository.searchByItemNoOrDescription(searchTerm);
    }

    /**
     * Add barcode to product
     */
    public Barcode addBarcodeToProduct(Long productId, String barcode, Boolean isPrimary) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            // Check if barcode already exists
            if (barcodeRepository.existsByBarcode(barcode)) {
                throw new RuntimeException("Barcode already exists: " + barcode);
            }

            Barcode newBarcode = new Barcode(barcode, productOpt.get(), isPrimary);
            return barcodeRepository.save(newBarcode);
        }
        throw new RuntimeException("Product not found with id: " + productId);
    }

    /**
     * Get all barcodes for a product
     */
    public List<Barcode> getProductBarcodes(Long productId) {
        return barcodeRepository.findAll().stream()
                .filter(b -> b.getProduct().getId().equals(productId))
                .collect(Collectors.toList());
    }

    /**
     * Remove barcode from product
     */
    public void removeBarcode(Long barcodeId) {
        barcodeRepository.deleteById(barcodeId);
    }

    /**
     * Set primary barcode for product
     */
    public Barcode setPrimaryBarcode(Long productId, Long barcodeId) {
        // Get all barcodes for the product
        List<Barcode> barcodes = getProductBarcodes(productId);

        // Set all to non-primary
        for (Barcode barcode : barcodes) {
            barcode.setIsPrimary(barcode.getId().equals(barcodeId));
            barcodeRepository.save(barcode);
        }

        // Return the new primary barcode
        return barcodeRepository.findById(barcodeId)
                .orElseThrow(() -> new RuntimeException("Barcode not found"));
    }

    /**
     * Find product by barcode with inventory check
     */
    public Product findProductByBarcodeWithInventoryCheck(String barcode) {
        Product product = smartBarcodeSearch(barcode);
        if (product != null && product.getInventoryQuantity() > 0) {
            return product;
        }
        return null;
    }

    /**
     * Quick product lookup for POS - returns minimal info for fast scanning
     */
    public ProductQuickInfo quickProductLookup(String barcode) {
        Product product = smartBarcodeSearch(barcode);
        if (product != null) {
            return new ProductQuickInfo(
                    product.getId(),
                    product.getItemNo(),
                    product.getDescription(),
                    product.getUnitPrice(),
                    product.getInventoryQuantity(),
                    product.getPhoto()
            );
        }
        return null;
    }

    // Quick info DTO for fast barcode scanning
    public static class ProductQuickInfo {
        private Long id;
        private String itemNo;
        private String description;
        private Double unitPrice;
        private Integer inventoryQuantity;
        private String photo;

        public ProductQuickInfo(Long id, String itemNo, String description, Double unitPrice, Integer inventoryQuantity, String photo) {
            this.id = id;
            this.itemNo = itemNo;
            this.description = description;
            this.unitPrice = unitPrice;
            this.inventoryQuantity = inventoryQuantity;
            this.photo = photo;
        }

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getItemNo() { return itemNo; }
        public void setItemNo(String itemNo) { this.itemNo = itemNo; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
        public Integer getInventoryQuantity() { return inventoryQuantity; }
        public void setInventoryQuantity(Integer inventoryQuantity) { this.inventoryQuantity = inventoryQuantity; }
        public String getPhoto() { return photo; }
        public void setPhoto(String photo) { this.photo = photo; }
    }
}