package com.jmdt.stockmanager.service;

import com.jmdt.stockmanager.models.*;
import com.jmdt.stockmanager.enums.StockChangeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for comprehensive inventory management
 * Handles stock tracking, warehouse management, batch tracking, and inventory operations
 */
public interface InventoryService {

    // ===== WAREHOUSE MANAGEMENT =====
    
    /**
     * Create a new warehouse for a business
     */
    Warehouse createWarehouse(Long businessId, Warehouse warehouse);
    
    /**
     * Create a new warehouse for a business
     */
    Warehouse createWarehouse(Warehouse warehouse);
    
    /**
     * Update warehouse details
     */
    Warehouse updateWarehouse(Long warehouseId, Warehouse warehouse);
    
    /**
     * Get warehouse by ID
     */
    Optional<Warehouse> getWarehouseByIdOptional(Long warehouseId);
    
    /**
     * Get warehouse by ID (throws exception if not found)
     */
    Warehouse getWarehouseById(Long warehouseId);
    
    /**
     * Get all warehouses for a business
     */
    List<Warehouse> getWarehousesByBusiness(Long businessId);
    
    /**
     * Get all warehouses for a business with pagination
     */
    Page<Warehouse> getWarehousesByBusiness(Long businessId, Pageable pageable);
    
    /**
     * Get active warehouses for a business
     */
    List<Warehouse> getActiveWarehousesByBusiness(Long businessId);
    
    /**
     * Deactivate warehouse (soft delete)
     */
    void deactivateWarehouse(Long warehouseId);
    
    /**
     * Delete warehouse
     */
    void deleteWarehouse(Long warehouseId);
    
    // ===== INVENTORY LOCATION MANAGEMENT =====
    
    /**
     * Get inventory location for product and warehouse
     */
    Optional<InventoryLocation> getInventoryLocation(Long productId, Long warehouseId);
    
    /**
     * Get all inventory locations for a product
     */
    List<InventoryLocation> getInventoryLocationsByProduct(Long productId);
    
    /**
     * Get all inventory locations for a warehouse
     */
    List<InventoryLocation> getInventoryLocationsByWarehouse(Long warehouseId);
    
    /**
     * Get all inventory locations for a warehouse with pagination
     */
    Page<InventoryLocation> getInventoryLocationsByWarehouse(Long warehouseId, Pageable pageable);
    
    /**
     * Update inventory location quantity
     */
    InventoryLocation updateInventoryLocation(Long productId, Long warehouseId, Integer quantity);
    
    /**
     * Transfer stock between warehouses
     */
    void transferStockVoid(Long productId, Long fromWarehouseId, Long toWarehouseId, Integer quantity, String remarks);
    
    /**
     * Transfer stock between warehouses (returns result map)
     */
    Map<String, Object> transferStock(Long productId, Long fromWarehouseId, Long toWarehouseId, Integer quantity, String reason);
    
    // ===== BATCH MANAGEMENT =====
    
    /**
     * Create a new product batch
     */
    ProductBatch createProductBatch(ProductBatch batch);
    
    /**
     * Update product batch
     */
    ProductBatch updateProductBatch(Long batchId, ProductBatch batch);
    
    /**
     * Get batch by ID
     */
    Optional<ProductBatch> getBatchById(Long batchId);
    
    /**
     * Get all batches for a product
     */
    List<ProductBatch> getBatchesByProduct(Long productId);
    
    /**
     * Get batches by warehouse
     */
    List<ProductBatch> getBatchesByWarehouse(Long warehouseId);
    
    /**
     * Get expiring batches within specified days
     */
    List<ProductBatch> getExpiringBatches(Long businessId, Integer daysFromNow);
    
    /**
     * Get expired batches
     */
    List<ProductBatch> getExpiredBatches(Long businessId);
    
    /**
     * Update batch quantity
     */
    ProductBatch updateBatchQuantity(Long batchId, Integer newQuantity);
    
    // ===== STOCK LOG MANAGEMENT =====
    
    /**
     * Create stock log entry
     */
    StockLog createStockLog(StockLog stockLog);
    
    /**
     * Get stock logs for a product
     */
    Page<StockLog> getStockLogsByProduct(Long productId, Pageable pageable);
    
    /**
     * Get stock logs by change type
     */
    Page<StockLog> getStockLogsByChangeType(Long businessId, StockChangeType changeType, Pageable pageable);
    
    /**
     * Get stock logs for date range
     */
    Page<StockLog> getStockLogsByDateRange(Long businessId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Get stock logs by user
     */
    Page<StockLog> getStockLogsByUser(Long userId, Pageable pageable);
    
    // ===== STOCK OPERATIONS =====
    
    /**
     * Add stock to inventory
     */
    void addStockVoid(Long productId, Long warehouseId, Integer quantity, String remarks, Long userId);
    
    /**
     * Add stock with batch information
     */
    void addStockWithBatch(Long productId, Long warehouseId, Integer quantity, Long batchId, String remarks, Long userId);
    
    /**
     * Remove stock from inventory
     */
    void removeStock(Long productId, Long warehouseId, Integer quantity, StockChangeType changeType, String remarks, Long userId);
    
    /**
     * Remove stock with batch information
     */
    void removeStockWithBatch(Long productId, Long warehouseId, Integer quantity, Long batchId, StockChangeType changeType, String remarks, Long userId);
    
    /**
     * Adjust stock quantity (can be positive or negative)
     */
    void adjustStock(Long productId, Long warehouseId, Integer adjustment, String remarks, Long userId);
    
    /**
     * Process sale stock reduction
     */
    void processSaleStockReduction(Long saleId);
    
    /**
     * Process return stock addition
     */
    void processReturnStockAddition(Long returnId);
    
    // ===== INVENTORY QUERIES =====
    
    /**
     * Get total stock for a product across all warehouses
     */
    Integer getTotalStockForProduct(Long productId);
    
    /**
     * Get stock for product in specific warehouse
     */
    Integer getStockForProductInWarehouse(Long productId, Long warehouseId);
    
    /**
     * Get low stock products for a business
     */
    List<Product> getLowStockProducts(Long businessId, Integer threshold);
    
    /**
     * Get out of stock products for a business
     */
    List<Product> getOutOfStockProducts(Long businessId);
    
    /**
     * Get stock summary for all products in a business
     */
    Map<Long, Integer> getStockSummaryByBusiness(Long businessId);
    
    /**
     * Get stock summary for a warehouse
     */
    Map<Long, Integer> getStockSummaryByWarehouse(Long warehouseId);
    
    /**
     * Check if sufficient stock is available for sale
     */
    boolean isStockAvailable(Long productId, Long warehouseId, Integer requiredQuantity);
    
    /**
     * Check stock availability across all warehouses
     */
    boolean isStockAvailableAcrossWarehouses(Long productId, Integer requiredQuantity);
    
    // ===== INVENTORY VALUATION =====
    
    /**
     * Calculate total inventory value for a business (at cost price)
     */
    Double calculateInventoryValueAtCost(Long businessId);
    
    /**
     * Calculate total inventory value for a business (at selling price)
     */
    Double calculateInventoryValueAtSellingPrice(Long businessId);
    
    /**
     * Calculate inventory value for a warehouse (at cost price)
     */
    Double calculateWarehouseInventoryValueAtCost(Long warehouseId);
    
    /**
     * Calculate inventory value for a warehouse (at selling price)
     */
    Double calculateWarehouseInventoryValueAtSellingPrice(Long warehouseId);
    
    /**
     * Get inventory valuation by product category
     */
    Map<String, Double> getInventoryValuationByCategory(Long businessId);
    
    // ===== INVENTORY SNAPSHOTS =====
    
    /**
     * Create daily inventory snapshot for all products
     */
    void createDailyInventorySnapshot(Long businessId, LocalDate snapshotDate);
    
    /**
     * Create inventory snapshot for specific product
     */
    InventorySnapshot createProductInventorySnapshot(Long productId, LocalDate snapshotDate);
    
    /**
     * Get inventory snapshots for date range
     */
    List<InventorySnapshot> getInventorySnapshotsByDateRange(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get inventory snapshots for a product
     */
    List<InventorySnapshot> getInventorySnapshotsByProduct(Long productId, LocalDate startDate, LocalDate endDate);
    
    // ===== INVENTORY ANALYTICS =====
    
    /**
     * Get stock movement analysis for a product
     */
    Map<String, Object> getStockMovementAnalysis(Long productId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get inventory turnover ratio for products
     */
    Map<Long, Double> getInventoryTurnoverRatio(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get slow-moving products
     */
    List<Product> getSlowMovingProducts(Long businessId, Integer daysThreshold);
    
    /**
     * Get fast-moving products
     */
    List<Product> getFastMovingProducts(Long businessId, Integer daysThreshold);
    
    /**
     * Get dead stock (no movement for specified days)
     */
    List<Product> getDeadStock(Long businessId, Integer daysThreshold);
    
    // ===== INVENTORY ALERTS =====
    
    /**
     * Get all inventory alerts for a business
     */
    Map<String, List<Product>> getInventoryAlerts(Long businessId);
    
    /**
     * Get reorder alerts (products below reorder level)
     */
    List<Product> getReorderAlerts(Long businessId);
    
    /**
     * Get overstock alerts (products above maximum stock level)
     */
    List<Product> getOverstockAlerts(Long businessId);
    
    /**
     * Get expiry alerts (products expiring soon)
     */
    List<ProductBatch> getExpiryAlerts(Long businessId, Integer daysThreshold);
    
    // ===== ADDITIONAL CONTROLLER METHODS =====
    
    /**
     * Add inventory location
     */
    InventoryLocation addInventoryLocation(Long warehouseId, Long productId, Integer quantity);
    
    /**
     * Update inventory quantity
     */
    InventoryLocation updateInventoryQuantity(Long locationId, Integer newQuantity, String reason);
    
    /**
     * Adjust stock (returns StockLog)
     */
    StockLog adjustStock(Long productId, Long warehouseId, Integer adjustmentQuantity, String reason);
    
    /**
     * Add stock (returns StockLog)
     */
    StockLog addStock(Long productId, Long warehouseId, Integer quantity, String reason, Long batchId);
    
    /**
     * Remove stock (returns StockLog)
     */
    StockLog removeStock(Long productId, Long warehouseId, Integer quantity, String reason, Long batchId);
    
    /**
     * Create product batch with product ID
     */
    ProductBatch createProductBatch(Long productId, ProductBatch batch);
    
    /**
     * Get product batches with pagination
     */
    Page<ProductBatch> getProductBatches(Long productId, Pageable pageable);
    
    /**
     * Get expiring batches with pagination
     */
    Page<ProductBatch> getExpiringBatches(Long businessId, int daysAhead, Pageable pageable);
    
    /**
     * Get expired batches with pagination
     */
    Page<ProductBatch> getExpiredBatches(Long businessId, Pageable pageable);
    
    /**
     * Get stock logs with filters
     */
    Page<StockLog> getStockLogs(Long productId, Long warehouseId, String changeType, 
                               LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Get product stock history
     */
    Page<StockLog> getProductStockHistory(Long productId, Pageable pageable);
    
    /**
     * Get stock levels analytics
     */
    Map<String, Object> getStockLevelsAnalytics(Long businessId);
    
    /**
     * Get low stock products with pagination
     */
    Page<Product> getLowStockProducts(Long businessId, int threshold, Pageable pageable);
    
    /**
     * Get out of stock products with pagination
     */
    Page<Product> getOutOfStockProducts(Long businessId, Pageable pageable);
    
    /**
     * Get inventory value
     */
    Map<String, Object> getInventoryValue(Long businessId);
    
    /**
     * Get inventory turnover rate
     */
    Map<String, Object> getInventoryTurnoverRate(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get stock movement report
     */
    Map<String, Object> getStockMovementReport(Long businessId, LocalDate startDate, LocalDate endDate, Long warehouseId);
    
    /**
     * Get inventory aging report
     */
    Map<String, Object> getInventoryAgingReport(Long businessId);
    
    /**
     * Get dead stock report
     */
    Map<String, Object> getDeadStockReport(Long businessId, int daysThreshold);
    
    /**
     * Export inventory data
     */
    byte[] exportInventory(Long businessId, String format, Long warehouseId, String filters);
    
    /**
     * Bulk stock update
     */
    Map<String, Object> bulkStockUpdate(Long businessId, List<Map<String, Object>> stockUpdates);
    
    /**
     * Bulk stock transfer
     */
    Map<String, Object> bulkStockTransfer(Long businessId, Long fromWarehouseId, Long toWarehouseId, 
                                        List<Map<String, Object>> transfers);
}
