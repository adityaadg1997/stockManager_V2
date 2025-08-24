package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.Sale;
import com.jmdt.stockmanager.enums.SaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    
    List<Sale> findByBusinessId(Long businessId);
    
    @Query("SELECT s FROM Sale s WHERE s.business.id = :businessId ORDER BY s.createdAt DESC")
    List<Sale> findByBusinessIdOrderByCreatedAtDesc(@Param("businessId") Long businessId);
    
    @Query("SELECT s FROM Sale s WHERE s.business.id = :businessId ORDER BY s.createdAt DESC")
    Page<Sale> findByBusinessIdOrderByCreatedAtDesc(@Param("businessId") Long businessId, Pageable pageable);
    
    List<Sale> findByBusinessIdAndStatus(Long businessId, SaleStatus status);
    
    @Query("SELECT s FROM Sale s WHERE s.business.id = :businessId AND s.status = :status ORDER BY s.createdAt DESC")
    List<Sale> findByBusinessIdAndStatusOrderByCreatedAtDesc(@Param("businessId") Long businessId, @Param("status") SaleStatus status);
    
    List<Sale> findByCustomerId(Long customerId);
    
    @Query("SELECT s FROM Sale s WHERE s.customer.id = :customerId ORDER BY s.createdAt DESC")
    List<Sale> findByCustomerIdOrderByCreatedAtDesc(@Param("customerId") Long customerId);
    
    @Query("SELECT s FROM Sale s WHERE s.customer.id = :customerId ORDER BY s.createdAt DESC")
    Page<Sale> findByCustomerIdOrderByCreatedAtDesc(@Param("customerId") Long customerId, Pageable pageable);
    
    Optional<Sale> findByInvoiceNo(String invoiceNo);
    
    @Query("SELECT s FROM Sale s WHERE s.business.id = :businessId AND s.saleDate BETWEEN :startDate AND :endDate")
    List<Sale> findByBusinessIdAndSaleDateBetween(@Param("businessId") Long businessId, 
                                                  @Param("startDate") LocalDateTime startDate, 
                                                  @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT s FROM Sale s WHERE s.business.id = :businessId AND s.saleDate BETWEEN :startDate AND :endDate ORDER BY s.createdAt DESC")
    List<Sale> findByBusinessIdAndSaleDateBetweenOrderByCreatedAtDesc(@Param("businessId") Long businessId, 
                                                                      @Param("startDate") LocalDateTime startDate, 
                                                                      @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT s FROM Sale s WHERE s.business.id = :businessId AND s.createdBy.id = :userId")
    List<Sale> findByBusinessIdAndCreatedBy(@Param("businessId") Long businessId, @Param("userId") Long userId);
    
    // Analytics queries
    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.business.id = :businessId AND s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalSalesAmount(@Param("businessId") Long businessId, 
                                   @Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(s) FROM Sale s WHERE s.business.id = :businessId AND s.saleDate BETWEEN :startDate AND :endDate")
    Long getTotalSalesCount(@Param("businessId") Long businessId, 
                           @Param("startDate") LocalDateTime startDate, 
                           @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT s FROM Sale s WHERE s.business.id = :businessId ORDER BY s.totalAmount DESC")
    List<Sale> findTopSalesByAmount(@Param("businessId") Long businessId, Pageable pageable);
}
