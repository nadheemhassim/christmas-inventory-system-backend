package com.example.inventory.services;

import com.example.inventory.dto.StockReportDTO;
import com.example.inventory.entity.Product;
import com.example.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepository;

    public List<StockReportDTO> getStockReport() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToStockReportDTO)
                .collect(Collectors.toList());
    }

    public List<StockReportDTO> getAllProductsForStockReport() {
        return getStockReport();
    }

    private StockReportDTO convertToStockReportDTO(Product product) {
        StockReportDTO dto = new StockReportDTO();
        dto.setProductId(product.getId());
        dto.setItemNo(product.getItemNo());
        dto.setDescription(product.getDescription());
        dto.setPhotoUrl(product.getPhoto());
        dto.setWarehouseLocation(product.getWarehouse() != null ? product.getWarehouse().getLocation() : "No Warehouse");
        dto.setWarehouseManager(product.getWarehouse() != null ? product.getWarehouse().getManager() : "N/A");
        dto.setWarehouseContact(product.getWarehouse() != null ? product.getWarehouse().getContactNumber() : "N/A");
        dto.setTotalProducts(product.getTotalProducts());
        dto.setAvailableQuantity(product.getInventoryQuantity());
        dto.setReservedQuantity(Math.max(0, (product.getOrderQuantity() - product.getInventoryQuantity())));
        dto.setCartons(product.getCartons());
        dto.setBoxesPerCarton(product.getBoxesPerCarton());
        dto.setQtyPerBox(product.getQtyPerBox());
        dto.setUnitPrice(product.getUnitPrice());
        dto.setVolume(product.getVolume());
        dto.setLength(product.getLength());
        dto.setWidth(product.getWidth());
        dto.setHeight(product.getHeight());
        dto.setClearing(product.getClearing());
        dto.setDuty(product.getDuty());
        dto.setLabourCharge(product.getLabourCharge());
        dto.setBankCharge(product.getBankCharge());
        dto.setDiscountCharge(product.getDiscountCharge());

        return dto;
    }
}