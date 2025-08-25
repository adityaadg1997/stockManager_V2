package com.jmdt.stockmanager.service.impl;

import com.jmdt.stockmanager.dto.request.ProductRequestDTO;
import com.jmdt.stockmanager.models.*;
import com.jmdt.stockmanager.repository.*;
import com.jmdt.stockmanager.service.ProductService;
import com.jmdt.stockmanager.exception.ResourceNotFoundException;
import com.jmdt.stockmanager.exception.StockManagerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of ProductService for product management
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductBatchRepository productBatchRepository;
    private final BusinessRepository businessRepository;
    private final VendorRepository vendorRepository;

    // ===== PRODUCT CRUD OPERATIONS =====

    @Override
    public Product createProduct(ProductRequestDTO request) {
        log.info("Creating new product: {}", request.getName());
        
        // Validate business exists
        if (!businessRepository.existsById(request.getBusinessId())) {
            throw new ResourceNotFoundException("Business not found with id: " + request.getBusinessId());
        }
        
        // Validate vendor if provided
        if (request.getVendorId() != null && !vendorRepository.existsById(request.getVendorId())) {
            throw new ResourceNotFoundException("Vendor not found with id: " + request.getVendorId());
        }
        Product product = new Product();
        // Set default values
        if (request.getQuantity() == null) {
            product.setQuantity(0);
        }
        if (request.getIsActive() == null) {
            product.setIsActive(true);
        }
        if (request.getHasWarranty() == null) {
            product.setHasWarranty(false);
        }
        if (request.getBatchTracked() == null) {
            product.setBatchTracked(false);
        }
        
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with id: {}", savedProduct.getId());
        return savedProduct;
    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        log.info("Updating product with id: {}", productId);
        
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        // Update fields
        existingProduct.setName(product.getName());
        existingProduct.setSkuCode(product.getSkuCode());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setCostPrice(product.getCostPrice());
        existingProduct.setSellingPrice(product.getSellingPrice());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setIsActive(product.getIsActive());
        existingProduct.setHasWarranty(product.getHasWarranty());
        existingProduct.setWarrantyDurationDays(product.getWarrantyDurationDays());
        existingProduct.setBatchTracked(product.getBatchTracked());
        
        if (product.getVendor() != null) {
            existingProduct.setVendor(product.getVendor());
        }
        
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated successfully: {}", updatedProduct.getId());
        return updatedProduct;
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public List<Product> getProductsByBusinessId(Long businessId) {
        return productRepository.findByBusinessId(businessId);
    }

    @Override
    public List<Product> getActiveProductsByBusinessId(Long businessId) {
        return productRepository.findByBusinessIdAndIsActive(businessId, true);
    }

    @Override
    public void deleteProduct(Long productId) {
        log.info("Deleting product with id: {}", productId);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        productRepository.delete(product);
        log.info("Product deleted successfully: {}", productId);
    }

    // ===== PRODUCT SEARCH AND FILTERING =====

    @Override
    public List<Product> searchProductsByName(Long businessId, String name) {
        return productRepository.findByBusinessIdAndNameContaining(businessId, name);
    }

    @Override
    public List<Product> getProductsByCategory(Long businessId, String category) {
        return productRepository.findByBusinessIdAndCategory(businessId, category);
    }

    @Override
    public List<Product> getProductsByVendor(Long businessId, Long vendorId) {
        return productRepository.findByBusinessIdAndVendorId(businessId, vendorId);
    }

    @Override
    public List<Product> getLowStockProducts(Long businessId, Integer threshold) {
        return productRepository.findLowStockProducts(businessId, threshold);
    }

    // ===== PRODUCT VARIANTS =====

    @Override
    public ProductVariant createProductVariant(ProductVariant variant) {
        log.info("Creating product variant for product id: {}", variant.getProduct().getId());
        
        // Validate product exists
        if (!productRepository.existsById(variant.getProduct().getId())) {
            throw new ResourceNotFoundException("Product not found with id: " + variant.getProduct().getId());
        }
        
        // Set default values
        if (variant.getQuantity() == null) {
            variant.setQuantity(0);
        }
        if (variant.getPriceAdjustment() == null) {
            variant.setPriceAdjustment(java.math.BigDecimal.ZERO);
        }
        
        return productVariantRepository.save(variant);
    }

    @Override
    public List<ProductVariant> getVariantsByProductId(Long productId) {
        return productVariantRepository.findByProductId(productId);
    }

    @Override
    public ProductVariant updateProductVariant(Long variantId, ProductVariant variant) {
        log.info("Updating product variant with id: {}", variantId);
        
        ProductVariant existingVariant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found with id: " + variantId));
        
        existingVariant.setName(variant.getName());
        existingVariant.setSkuSuffix(variant.getSkuSuffix());
        existingVariant.setQuantity(variant.getQuantity());
        existingVariant.setPriceAdjustment(variant.getPriceAdjustment());
        
        return productVariantRepository.save(existingVariant);
    }

    @Override
    public void deleteProductVariant(Long variantId) {
        log.info("Deleting product variant with id: {}", variantId);
        
        if (!productVariantRepository.existsById(variantId)) {
            throw new ResourceNotFoundException("Product variant not found with id: " + variantId);
        }
        
        productVariantRepository.deleteById(variantId);
        log.info("Product variant deleted successfully: {}", variantId);
    }

    // ===== PRODUCT BATCHES =====

    @Override
    public ProductBatch createProductBatch(ProductBatch batch) {
        log.info("Creating product batch for product id: {}", batch.getProduct().getId());
        
        // Validate product exists and is batch tracked
        Product product = productRepository.findById(batch.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + batch.getProduct().getId()));
        
        if (!product.getBatchTracked()) {
            throw new StockManagerException("Product is not configured for batch tracking");
        }
        
        // Set default quantity if not provided
        if (batch.getQuantity() == null) {
            batch.setQuantity(0);
        }
        
        return productBatchRepository.save(batch);
    }

    @Override
    public List<ProductBatch> getBatchesByProductId(Long productId) {
        return productBatchRepository.findByProductId(productId);
    }

    @Override
    public List<ProductBatch> getExpiringBatches(Long businessId, int daysFromNow) {
        java.time.LocalDate expiryThreshold = java.time.LocalDate.now().plusDays(daysFromNow);
        return productBatchRepository.findBatchesExpiringBefore(businessId, expiryThreshold);
    }

    @Override
    public ProductBatch updateProductBatch(Long batchId, ProductBatch batch) {
        log.info("Updating product batch with id: {}", batchId);
        
        ProductBatch existingBatch = productBatchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + batchId));
        
        existingBatch.setBatchNumber(batch.getBatchNumber());
        existingBatch.setManufacturingDate(batch.getManufacturingDate());
        existingBatch.setExpiryDate(batch.getExpiryDate());
        existingBatch.setQuantity(batch.getQuantity());
        existingBatch.setWarehouse(batch.getWarehouse());
        
        return productBatchRepository.save(existingBatch);
    }

    @Override
    public void deleteProductBatch(Long batchId) {
        log.info("Deleting product batch with id: {}", batchId);
        
        if (!productBatchRepository.existsById(batchId)) {
            throw new ResourceNotFoundException("Product batch not found with id: " + batchId);
        }
        
        productBatchRepository.deleteById(batchId);
        log.info("Product batch deleted successfully: {}", batchId);
    }

    // ===== STOCK MANAGEMENT =====

    @Override
    public void updateProductStock(Long productId, Integer quantity) {
        log.info("Updating stock for product id: {} to quantity: {}", productId, quantity);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        if (quantity < 0) {
            throw new StockManagerException("Stock quantity cannot be negative");
        }
        
        product.setQuantity(quantity);
        productRepository.save(product);
        
        log.info("Product stock updated successfully for id: {}", productId);
    }

    @Override
    public void adjustProductStock(Long productId, Integer adjustment, String reason) {
        log.info("Adjusting stock for product id: {} by: {} (reason: {})", productId, adjustment, reason);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        int newQuantity = product.getQuantity() + adjustment;
        if (newQuantity < 0) {
            throw new StockManagerException("Insufficient stock. Current: " + product.getQuantity() + ", Adjustment: " + adjustment);
        }
        
        product.setQuantity(newQuantity);
        productRepository.save(product);
        
        log.info("Product stock adjusted successfully for id: {}", productId);
    }

    // ===== BUSINESS LOGIC =====

    @Override
    public boolean isSkuCodeUnique(Long businessId, String skuCode) {
        return !productRepository.findBySkuCodeAndBusinessId(skuCode, businessId).isPresent();
    }

    @Override
    public void activateProduct(Long productId) {
        log.info("Activating product with id: {}", productId);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        product.setIsActive(true);
        productRepository.save(product);
        
        log.info("Product activated successfully: {}", productId);
    }

    @Override
    public void deactivateProduct(Long productId) {
        log.info("Deactivating product with id: {}", productId);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        product.setIsActive(false);
        productRepository.save(product);
        
        log.info("Product deactivated successfully: {}", productId);
    }
}
