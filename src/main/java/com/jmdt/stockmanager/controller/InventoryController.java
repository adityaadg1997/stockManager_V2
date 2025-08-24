package com.jmdt.stockmanager.controller;

import com.jmdt.stockmanager.exception.ResourceNotFoundException;
import com.jmdt.stockmanager.models.*;
import com.jmdt.stockmanager.payloads.ApiResponse;
import com.jmdt.stockmanager.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // ==================== WAREHOUSE MANAGEMENT ====================

    @PostMapping("/business/{businessId}/warehouses")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> createWarehouse(
            @PathVariable Long businessId,
            @Valid @RequestBody Warehouse warehouse) {
        try {
            Warehouse createdWarehouse = inventoryService.createWarehouse(businessId, warehouse);
            ApiResponse response = new ApiResponse("Warehouse created successfully", true);
            response.setData(createdWarehouse);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to create warehouse: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/warehouses/{warehouseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getWarehouseById(@PathVariable Long warehouseId) {
        try {
            Warehouse warehouse = inventoryService.getWarehouseById(warehouseId);
            ApiResponse response = new ApiResponse("Warehouse retrieved successfully", true);
            response.setData(warehouse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Warehouse not found: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/business/{businessId}/warehouses")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getWarehousesByBusiness(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Warehouse> warehouses = inventoryService.getWarehousesByBusiness(businessId, pageable);
            ApiResponse response = new ApiResponse("Warehouses retrieved successfully", true);
            response.setData(warehouses);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve warehouses: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/warehouses/{warehouseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> updateWarehouse(
            @PathVariable Long warehouseId,
            @Valid @RequestBody Warehouse warehouseDetails) {
        try {
            Warehouse updatedWarehouse = inventoryService.updateWarehouse(warehouseId, warehouseDetails);
            ApiResponse response = new ApiResponse("Warehouse updated successfully", true);
            response.setData(updatedWarehouse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to update warehouse: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/warehouses/{warehouseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteWarehouse(@PathVariable Long warehouseId) {
        try {
            inventoryService.deleteWarehouse(warehouseId);
            ApiResponse response = new ApiResponse("Warehouse deleted successfully", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to delete warehouse: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== INVENTORY LOCATION MANAGEMENT ====================

    @PostMapping("/warehouses/{warehouseId}/locations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> addInventoryLocation(
            @PathVariable Long warehouseId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        try {
            InventoryLocation location = inventoryService.addInventoryLocation(warehouseId, productId, quantity);
            ApiResponse response = new ApiResponse("Inventory location added successfully", true);
            response.setData(location);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to add inventory location: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/warehouses/{warehouseId}/locations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getInventoryLocationsByWarehouse(
            @PathVariable Long warehouseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<InventoryLocation> locations = inventoryService.getInventoryLocationsByWarehouse(warehouseId, pageable);
            ApiResponse response = new ApiResponse("Inventory locations retrieved successfully", true);
            response.setData(locations);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve inventory locations: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/products/{productId}/locations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getInventoryLocationsByProduct(@PathVariable Long productId) {
        try {
            List<InventoryLocation> locations = inventoryService.getInventoryLocationsByProduct(productId);
            ApiResponse response = new ApiResponse("Product locations retrieved successfully", true);
            response.setData(locations);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve product locations: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/locations/{locationId}/quantity")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> updateInventoryQuantity(
            @PathVariable Long locationId,
            @RequestParam Integer newQuantity,
            @RequestParam(required = false) String reason) {
        try {
            InventoryLocation location = inventoryService.updateInventoryQuantity(locationId, newQuantity, reason);
            ApiResponse response = new ApiResponse("Inventory quantity updated successfully", true);
            response.setData(location);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to update inventory quantity: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== STOCK MOVEMENT OPERATIONS ====================

    @PostMapping("/stock/transfer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> transferStock(
            @RequestParam Long productId,
            @RequestParam Long fromWarehouseId,
            @RequestParam Long toWarehouseId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String reason) {
        try {
            Map<String, Object> result = inventoryService.transferStock(
                productId, fromWarehouseId, toWarehouseId, quantity, reason);
            ApiResponse response = new ApiResponse("Stock transferred successfully", true);
            response.setData(result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to transfer stock: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/stock/adjust")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> adjustStock(
            @RequestParam Long productId,
            @RequestParam Long warehouseId,
            @RequestParam Integer adjustmentQuantity,
            @RequestParam String reason) {
        try {
            StockLog stockLog = inventoryService.adjustStock(productId, warehouseId, adjustmentQuantity, reason);
            ApiResponse response = new ApiResponse("Stock adjusted successfully", true);
            response.setData(stockLog);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to adjust stock: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/stock/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> addStock(
            @RequestParam Long productId,
            @RequestParam Long warehouseId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String reason,
            @RequestParam(required = false) Long batchId) {
        try {
            StockLog stockLog = inventoryService.addStock(productId, warehouseId, quantity, reason, batchId);
            ApiResponse response = new ApiResponse("Stock added successfully", true);
            response.setData(stockLog);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to add stock: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/stock/remove")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> removeStock(
            @RequestParam Long productId,
            @RequestParam Long warehouseId,
            @RequestParam Integer quantity,
            @RequestParam String reason,
            @RequestParam(required = false) Long batchId) {
        try {
            StockLog stockLog = inventoryService.removeStock(productId, warehouseId, quantity, reason, batchId);
            ApiResponse response = new ApiResponse("Stock removed successfully", true);
            response.setData(stockLog);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to remove stock: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== BATCH MANAGEMENT ====================

    @PostMapping("/products/{productId}/batches")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> createProductBatch(
            @PathVariable Long productId,
            @Valid @RequestBody ProductBatch batch) {
        try {
            ProductBatch createdBatch = inventoryService.createProductBatch(productId, batch);
            ApiResponse response = new ApiResponse("Product batch created successfully", true);
            response.setData(createdBatch);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to create product batch: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/products/{productId}/batches")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getProductBatches(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductBatch> batches = inventoryService.getProductBatches(productId, pageable);
            ApiResponse response = new ApiResponse("Product batches retrieved successfully", true);
            response.setData(batches);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve product batches: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/batches/expiring")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getExpiringBatches(
            @RequestParam Long businessId,
            @RequestParam(defaultValue = "30") int daysAhead,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductBatch> expiringBatches = inventoryService.getExpiringBatches(businessId, daysAhead, pageable);
            ApiResponse response = new ApiResponse("Expiring batches retrieved successfully", true);
            response.setData(expiringBatches);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve expiring batches: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/batches/expired")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getExpiredBatches(
            @RequestParam Long businessId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductBatch> expiredBatches = inventoryService.getExpiredBatches(businessId, pageable);
            ApiResponse response = new ApiResponse("Expired batches retrieved successfully", true);
            response.setData(expiredBatches);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve expired batches: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== STOCK LOGS AND AUDIT ====================

    @GetMapping("/stock-logs")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getStockLogs(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) String changeType,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<StockLog> stockLogs = inventoryService.getStockLogs(
                productId, warehouseId, changeType, startDate, endDate, pageable);
            ApiResponse response = new ApiResponse("Stock logs retrieved successfully", true);
            response.setData(stockLogs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve stock logs: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/products/{productId}/stock-history")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getProductStockHistory(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<StockLog> stockHistory = inventoryService.getProductStockHistory(productId, pageable);
            ApiResponse response = new ApiResponse("Product stock history retrieved successfully", true);
            response.setData(stockHistory);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve product stock history: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== INVENTORY ANALYTICS ====================

    @GetMapping("/business/{businessId}/analytics/stock-levels")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getStockLevelsAnalytics(@PathVariable Long businessId) {
        try {
            Map<String, Object> analytics = inventoryService.getStockLevelsAnalytics(businessId);
            ApiResponse response = new ApiResponse("Stock levels analytics retrieved successfully", true);
            response.setData(analytics);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve stock analytics: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/analytics/low-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getLowStockProducts(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "10") int threshold,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> lowStockProducts = inventoryService.getLowStockProducts(businessId, threshold, pageable);
            ApiResponse response = new ApiResponse("Low stock products retrieved successfully", true);
            response.setData(lowStockProducts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve low stock products: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/analytics/out-of-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getOutOfStockProducts(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> outOfStockProducts = inventoryService.getOutOfStockProducts(businessId, pageable);
            ApiResponse response = new ApiResponse("Out of stock products retrieved successfully", true);
            response.setData(outOfStockProducts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve out of stock products: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/analytics/inventory-value")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getInventoryValue(@PathVariable Long businessId) {
        try {
            Map<String, Object> inventoryValue = inventoryService.getInventoryValue(businessId);
            ApiResponse response = new ApiResponse("Inventory value calculated successfully", true);
            response.setData(inventoryValue);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to calculate inventory value: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/analytics/turnover-rate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getInventoryTurnoverRate(
            @PathVariable Long businessId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        try {
            Map<String, Object> turnoverRate = inventoryService.getInventoryTurnoverRate(businessId, startDate, endDate);
            ApiResponse response = new ApiResponse("Inventory turnover rate calculated successfully", true);
            response.setData(turnoverRate);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to calculate turnover rate: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== INVENTORY REPORTS ====================

    @GetMapping("/business/{businessId}/reports/stock-movement")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getStockMovementReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) Long warehouseId) {
        try {
            Map<String, Object> report = inventoryService.getStockMovementReport(businessId, startDate, endDate, warehouseId);
            ApiResponse response = new ApiResponse("Stock movement report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate stock movement report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/reports/inventory-aging")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getInventoryAgingReport(@PathVariable Long businessId) {
        try {
            Map<String, Object> report = inventoryService.getInventoryAgingReport(businessId);
            ApiResponse response = new ApiResponse("Inventory aging report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate inventory aging report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/reports/dead-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getDeadStockReport(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "90") int daysThreshold) {
        try {
            Map<String, Object> report = inventoryService.getDeadStockReport(businessId, daysThreshold);
            ApiResponse response = new ApiResponse("Dead stock report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate dead stock report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== INVENTORY EXPORT ====================

    @GetMapping("/business/{businessId}/export")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> exportInventory(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "csv") String format,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) String filters) {
        try {
            byte[] exportData = inventoryService.exportInventory(businessId, format, warehouseId, filters);
            ApiResponse response = new ApiResponse("Inventory exported successfully", true);
            response.setData(Map.of(
                "format", format,
                "size", exportData.length,
                "data", exportData
            ));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Export failed: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== BULK OPERATIONS ====================

    @PostMapping("/business/{businessId}/bulk-stock-update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> bulkStockUpdate(
            @PathVariable Long businessId,
            @RequestBody List<Map<String, Object>> stockUpdates) {
        try {
            Map<String, Object> result = inventoryService.bulkStockUpdate(businessId, stockUpdates);
            ApiResponse response = new ApiResponse("Bulk stock update completed successfully", true);
            response.setData(result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Bulk stock update failed: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/business/{businessId}/bulk-transfer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> bulkStockTransfer(
            @PathVariable Long businessId,
            @RequestParam Long fromWarehouseId,
            @RequestParam Long toWarehouseId,
            @RequestBody List<Map<String, Object>> transfers) {
        try {
            Map<String, Object> result = inventoryService.bulkStockTransfer(
                businessId, fromWarehouseId, toWarehouseId, transfers);
            ApiResponse response = new ApiResponse("Bulk stock transfer completed successfully", true);
            response.setData(result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Bulk stock transfer failed: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
