package com.jmdt.stockmanager.service;

import com.jmdt.stockmanager.models.Product;
import com.jmdt.stockmanager.models.ProductVariant;
import com.jmdt.stockmanager.models.ProductBatch;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    
    // Product CRUD operations
    Product createProduct(Product product);
    Product updateProduct(Long productId, Product product);
    Optional<Product> getProductById(Long productId);
    List<Product> getProductsByBusinessId(Long businessId);
    List<Product> getActiveProductsByBusinessId(Long businessId);
    void deleteProduct(Long productId);
    
    // Product search and filtering
    List<Product> searchProductsByName(Long businessId, String name);
    List<Product> getProductsByCategory(Long businessId, String category);
    List<Product> getProductsByVendor(Long businessId, Long vendorId);
    List<Product> getLowStockProducts(Long businessId, Integer threshold);
    
    // Product variants
    ProductVariant createProductVariant(ProductVariant variant);
    List<ProductVariant> getVariantsByProductId(Long productId);
    ProductVariant updateProductVariant(Long variantId, ProductVariant variant);
    void deleteProductVariant(Long variantId);
    
    // Product batches
    ProductBatch createProductBatch(ProductBatch batch);
    List<ProductBatch> getBatchesByProductId(Long productId);
    List<ProductBatch> getExpiringBatches(Long businessId, int daysFromNow);
    ProductBatch updateProductBatch(Long batchId, ProductBatch batch);
    void deleteProductBatch(Long batchId);
    
    // Stock management
    void updateProductStock(Long productId, Integer quantity);
    void adjustProductStock(Long productId, Integer adjustment, String reason);
    
    // Business logic
    boolean isSkuCodeUnique(Long businessId, String skuCode);
    void activateProduct(Long productId);
    void deactivateProduct(Long productId);
}
