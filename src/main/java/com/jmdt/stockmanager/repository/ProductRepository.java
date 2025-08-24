package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Basic queries
    List<Product> findByBusinessId(Long businessId);
    
    List<Product> findByBusinessIdAndIsActive(Long businessId, Boolean isActive);
    
    Page<Product> findByBusinessIdAndIsActiveTrue(Long businessId, Pageable pageable);
    
    Optional<Product> findByBusinessIdAndSkuCode(Long businessId, String skuCode);
    
    Optional<Product> findBySkuCodeAndBusinessId(String skuCode, Long businessId);
    
    List<Product> findByBusinessIdAndCategory(Long businessId, String category);
    
    Page<Product> findByBusinessIdAndCategoryAndIsActiveTrue(Long businessId, String category, Pageable pageable);
    
    List<Product> findByBusinessIdAndVendorId(Long businessId, Long vendorId);
    
    Page<Product> findByVendorIdAndIsActiveTrue(Long vendorId, Pageable pageable);
    
    Long countByBusinessIdAndIsActiveTrue(Long businessId);
    
    // Search queries
    @Query("SELECT p FROM Product p WHERE p.business.id = :businessId AND p.name LIKE %:name%")
    List<Product> findByBusinessIdAndNameContaining(@Param("businessId") Long businessId, @Param("name") String name);
    
    @Query("SELECT p FROM Product p WHERE p.business.id = :businessId AND (p.name LIKE %:searchTerm% OR p.skuCode LIKE %:searchTerm%) AND p.isActive = true")
    Page<Product> findByBusinessIdAndNameContainingIgnoreCaseOrSkuCodeContainingIgnoreCase(
            @Param("businessId") Long businessId, 
            @Param("searchTerm") String searchTerm1, 
            @Param("searchTerm") String searchTerm2, 
            Pageable pageable);
    
    // Stock queries
    @Query("SELECT p FROM Product p WHERE p.business.id = :businessId AND p.quantity <= :threshold AND p.isActive = true")
    List<Product> findLowStockProducts(@Param("businessId") Long businessId, @Param("threshold") Integer threshold);
    
    @Query("SELECT p FROM Product p WHERE p.business.id = :businessId AND p.quantity = 0 AND p.isActive = true")
    List<Product> findOutOfStockProducts(@Param("businessId") Long businessId);
    
    // Analytics queries
    @Query("SELECT p.category, COUNT(p), SUM(p.quantity), SUM(p.quantity * p.costPrice) " +
           "FROM Product p WHERE p.business.id = :businessId AND p.isActive = true " +
           "GROUP BY p.category")
    List<Object[]> findCategoryWiseAnalytics(@Param("businessId") Long businessId);
    
    @Query("SELECT v.id, v.name, COUNT(p), SUM(p.quantity), SUM(p.quantity * p.costPrice) " +
           "FROM Product p LEFT JOIN p.vendor v WHERE p.business.id = :businessId AND p.isActive = true " +
           "GROUP BY v.id, v.name")
    List<Object[]> findVendorWiseAnalytics(@Param("businessId") Long businessId);
    
    @Query("SELECT p FROM Product p WHERE p.business.id = :businessId AND p.isActive = true ORDER BY p.quantity DESC")
    List<Product> findTopSellingProducts(@Param("businessId") Long businessId, @Param("limit") Integer limit);
    
    @Query("SELECT p FROM Product p WHERE p.business.id = :businessId AND p.createdAt < :thresholdDate AND p.isActive = true")
    List<Product> findSlowMovingProducts(@Param("businessId") Long businessId, @Param("thresholdDate") LocalDate thresholdDate);
}
