package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.ProductBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductBatchRepository extends JpaRepository<ProductBatch, Long> {
    
    List<ProductBatch> findByProductId(Long productId);
    
    List<ProductBatch> findByWarehouseId(Long warehouseId);
    
    @Query("SELECT pb FROM ProductBatch pb JOIN pb.product p WHERE p.business.id = :businessId AND pb.expiryDate <= :expiryThreshold AND pb.quantity > 0")
    List<ProductBatch> findBatchesExpiringBefore(@Param("businessId") Long businessId, @Param("expiryThreshold") LocalDate expiryThreshold);
    
    @Query("SELECT pb FROM ProductBatch pb JOIN pb.product p WHERE p.business.id = :businessId AND pb.expiryDate < :today AND pb.quantity > 0")
    List<ProductBatch> findExpiredBatches(@Param("businessId") Long businessId, @Param("today") LocalDate today);
}
