package com.jmdt.stockmanager.controller;

import com.jmdt.stockmanager.models.Product;
import com.jmdt.stockmanager.models.ProductVariant;
import com.jmdt.stockmanager.models.ProductBatch;
import com.jmdt.stockmanager.service.ProductService;
import com.jmdt.stockmanager.payloads.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Product Management
 * Provides comprehensive CRUD operations for products, variants, and batches
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    // ===== PRODUCT CRUD OPERATIONS =====

    /**
     * Create a new product
     * POST /api/products
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody Product product) {
        try {
            log.info("Creating new product: {}", product.getName());
            Product createdProduct = productService.createProduct(product);
            
            ApiResponse response = new ApiResponse("Product created successfully", true);
            response.setData(createdProduct);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to create product: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update an existing product
     * PUT /api/products/{productId}
     */
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product product) {
        try {
            log.info("Updating product with id: {}", productId);
            Product updatedProduct = productService.updateProduct(productId, product);
            
            ApiResponse response = new ApiResponse("Product updated successfully", true);
            response.setData(updatedProduct);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating product: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to update product: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get product by ID
     * GET /api/products/{productId}
     */
    @GetMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            log.info("Fetching product with id: {}", productId);
            Optional<Product> product = productService.getProductById(productId);
            
            if (product.isPresent()) {
                ApiResponse response = new ApiResponse("Product retrieved successfully", true);
                response.setData(product.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ApiResponse response = new ApiResponse("Product not found", false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error fetching product: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to fetch product: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all products for a business
     * GET /api/products/business/{businessId}
     */
    @GetMapping("/business/{businessId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getProductsByBusiness(@PathVariable Long businessId) {
        try {
            log.info("Fetching products for business id: {}", businessId);
            List<Product> products = productService.getProductsByBusinessId(businessId);
            
            ApiResponse response = new ApiResponse("Products retrieved successfully", true);
            response.setData(products);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching products: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to fetch products: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get active products for a business
     * GET /api/products/business/{businessId}/active
     */
    @GetMapping("/business/{businessId}/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getActiveProductsByBusiness(@PathVariable Long businessId) {
        try {
            log.info("Fetching active products for business id: {}", businessId);
            List<Product> products = productService.getActiveProductsByBusinessId(businessId);
            
            ApiResponse response = new ApiResponse("Active products retrieved successfully", true);
            response.setData(products);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching active products: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to fetch active products: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a product
     * DELETE /api/products/{productId}
     */
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            log.info("Deleting product with id: {}", productId);
            productService.deleteProduct(productId);
            
            ApiResponse response = new ApiResponse("Product deleted successfully", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error deleting product: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to delete product: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ===== PRODUCT SEARCH AND FILTERING =====

    /**
     * Search products by name
     * GET /api/products/business/{businessId}/search?name={name}
     */
    @GetMapping("/business/{businessId}/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> searchProductsByName(
            @PathVariable Long businessId,
            @RequestParam String name) {
        try {
            log.info("Searching products by name: {} for business: {}", name, businessId);
            List<Product> products = productService.searchProductsByName(businessId, name);
            
            ApiResponse response = new ApiResponse("Products search completed successfully", true);
            response.setData(products);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error searching products: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to search products: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get products by category
     * GET /api/products/business/{businessId}/category/{category}
     */
    @GetMapping("/business/{businessId}/category/{category}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getProductsByCategory(
            @PathVariable Long businessId,
            @PathVariable String category) {
        try {
            log.info("Fetching products by category: {} for business: {}", category, businessId);
            List<Product> products = productService.getProductsByCategory(businessId, category);
            
            ApiResponse response = new ApiResponse("Products by category retrieved successfully", true);
            response.setData(products);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching products by category: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to fetch products by category: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get products by vendor
     * GET /api/products/business/{businessId}/vendor/{vendorId}
     */
    @GetMapping("/business/{businessId}/vendor/{vendorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getProductsByVendor(
            @PathVariable Long businessId,
            @PathVariable Long vendorId) {
        try {
            log.info("Fetching products by vendor: {} for business: {}", vendorId, businessId);
            List<Product> products = productService.getProductsByVendor(businessId, vendorId);
            
            ApiResponse response = new ApiResponse("Products by vendor retrieved successfully", true);
            response.setData(products);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching products by vendor: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to fetch products by vendor: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get low stock products
     * GET /api/products/business/{businessId}/low-stock?threshold={threshold}
     */
    @GetMapping("/business/{businessId}/low-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getLowStockProducts(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "10") Integer threshold) {
        try {
            log.info("Fetching low stock products for business: {} with threshold: {}", businessId, threshold);
            List<Product> products = productService.getLowStockProducts(businessId, threshold);
            
            ApiResponse response = new ApiResponse("Low stock products retrieved successfully", true);
            response.setData(products);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching low stock products: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to fetch low stock products: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== PRODUCT VARIANTS =====

    /**
     * Create a product variant
     * POST /api/products/{productId}/variants
     */
    @PostMapping("/{productId}/variants")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> createProductVariant(
            @PathVariable Long productId,
            @RequestBody ProductVariant variant) {
        try {
            log.info("Creating variant for product id: {}", productId);
            ProductVariant createdVariant = productService.createProductVariant(variant);
            
            ApiResponse response = new ApiResponse("Product variant created successfully", true);
            response.setData(createdVariant);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating product variant: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to create product variant: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get variants for a product
     * GET /api/products/{productId}/variants
     */
    @GetMapping("/{productId}/variants")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getVariantsByProduct(@PathVariable Long productId) {
        try {
            log.info("Fetching variants for product id: {}", productId);
            List<ProductVariant> variants = productService.getVariantsByProductId(productId);
            
            ApiResponse response = new ApiResponse("Product variants retrieved successfully", true);
            response.setData(variants);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching product variants: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to fetch product variants: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update a product variant
     * PUT /api/products/variants/{variantId}
     */
    @PutMapping("/variants/{variantId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> updateProductVariant(
            @PathVariable Long variantId,
            @RequestBody ProductVariant variant) {
        try {
            log.info("Updating product variant with id: {}", variantId);
            ProductVariant updatedVariant = productService.updateProductVariant(variantId, variant);
            
            ApiResponse response = new ApiResponse("Product variant updated successfully", true);
            response.setData(updatedVariant);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating product variant: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to update product variant: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete a product variant
     * DELETE /api/products/variants/{variantId}
     */
    @DeleteMapping("/variants/{variantId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> deleteProductVariant(@PathVariable Long variantId) {
        try {
            log.info("Deleting product variant with id: {}", variantId);
            productService.deleteProductVariant(variantId);
            
            ApiResponse response = new ApiResponse("Product variant deleted successfully", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error deleting product variant: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to delete product variant: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ===== PRODUCT BATCHES =====

    /**
     * Create a product batch
     * POST /api/products/{productId}/batches
     */
    @PostMapping("/{productId}/batches")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> createProductBatch(
            @PathVariable Long productId,
            @Valid @RequestBody ProductBatch batch) {
        try {
            log.info("Creating batch for product id: {}", productId);
            ProductBatch createdBatch = productService.createProductBatch(batch);
            
            ApiResponse response = new ApiResponse("Product batch created successfully", true);
            response.setData(createdBatch);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating product batch: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to create product batch: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get batches for a product
     * GET /api/products/{productId}/batches
     */
    @GetMapping("/{productId}/batches")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getBatchesByProduct(@PathVariable Long productId) {
        try {
            log.info("Fetching batches for product id: {}", productId);
            List<ProductBatch> batches = productService.getBatchesByProductId(productId);
            
            ApiResponse response = new ApiResponse("Product batches retrieved successfully", true);
            response.setData(batches);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching product batches: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to fetch product batches: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get expiring batches
     * GET /api/products/business/{businessId}/batches/expiring?days={days}
     */
    @GetMapping("/business/{businessId}/batches/expiring")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getExpiringBatches(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "30") int days) {
        try {
            log.info("Fetching expiring batches for business: {} within {} days", businessId, days);
            List<ProductBatch> batches = productService.getExpiringBatches(businessId, days);
            
            ApiResponse response = new ApiResponse("Expiring batches retrieved successfully", true);
            response.setData(batches);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching expiring batches: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to fetch expiring batches: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update a product batch
     * PUT /api/products/batches/{batchId}
     */
    @PutMapping("/batches/{batchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> updateProductBatch(
            @PathVariable Long batchId,
            @Valid @RequestBody ProductBatch batch) {
        try {
            log.info("Updating product batch with id: {}", batchId);
            ProductBatch updatedBatch = productService.updateProductBatch(batchId, batch);
            
            ApiResponse response = new ApiResponse("Product batch updated successfully", true);
            response.setData(updatedBatch);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating product batch: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to update product batch: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete a product batch
     * DELETE /api/products/batches/{batchId}
     */
    @DeleteMapping("/batches/{batchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> deleteProductBatch(@PathVariable Long batchId) {
        try {
            log.info("Deleting product batch with id: {}", batchId);
            productService.deleteProductBatch(batchId);
            
            ApiResponse response = new ApiResponse("Product batch deleted successfully", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error deleting product batch: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to delete product batch: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ===== STOCK MANAGEMENT =====

    /**
     * Update product stock
     * PUT /api/products/{productId}/stock
     */
    @PutMapping("/{productId}/stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> updateProductStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        try {
            log.info("Updating stock for product id: {} to quantity: {}", productId, quantity);
            productService.updateProductStock(productId, quantity);
            
            ApiResponse response = new ApiResponse("Product stock updated successfully", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating product stock: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to update product stock: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Adjust product stock
     * POST /api/products/{productId}/stock/adjust
     */
    @PostMapping("/{productId}/stock/adjust")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> adjustProductStock(
            @PathVariable Long productId,
            @RequestParam Integer adjustment,
            @RequestParam(required = false) String reason) {
        try {
            log.info("Adjusting stock for product id: {} by: {}", productId, adjustment);
            productService.adjustProductStock(productId, adjustment, reason);
            
            ApiResponse response = new ApiResponse("Product stock adjusted successfully", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error adjusting product stock: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to adjust product stock: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ===== BUSINESS LOGIC =====

    /**
     * Check if SKU code is unique
     * GET /api/products/business/{businessId}/sku-unique?skuCode={skuCode}
     */
    @GetMapping("/business/{businessId}/sku-unique")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> isSkuCodeUnique(
            @PathVariable Long businessId,
            @RequestParam String skuCode) {
        try {
            log.info("Checking SKU uniqueness for business: {} and SKU: {}", businessId, skuCode);
            boolean isUnique = productService.isSkuCodeUnique(businessId, skuCode);
            
            ApiResponse response = new ApiResponse("SKU uniqueness check completed", true);
            response.setData(isUnique);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error checking SKU uniqueness: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to check SKU uniqueness: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Activate a product
     * POST /api/products/{productId}/activate
     */
    @PostMapping("/{productId}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> activateProduct(@PathVariable Long productId) {
        try {
            log.info("Activating product with id: {}", productId);
            productService.activateProduct(productId);
            
            ApiResponse response = new ApiResponse("Product activated successfully", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error activating product: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to activate product: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deactivate a product
     * POST /api/products/{productId}/deactivate
     */
    @PostMapping("/{productId}/deactivate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> deactivateProduct(@PathVariable Long productId) {
        try {
            log.info("Deactivating product with id: {}", productId);
            productService.deactivateProduct(productId);
            
            ApiResponse response = new ApiResponse("Product deactivated successfully", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error deactivating product: {}", e.getMessage());
            ApiResponse response = new ApiResponse("Failed to deactivate product: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
