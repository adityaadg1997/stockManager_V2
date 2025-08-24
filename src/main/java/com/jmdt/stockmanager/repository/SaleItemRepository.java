package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    
    List<SaleItem> findBySaleId(Long saleId);
    
    @Query("SELECT si FROM SaleItem si WHERE si.sale.business.id = :businessId AND si.warrantyEndsAt > :currentDate")
    List<SaleItem> findItemsUnderWarranty(@Param("businessId") Long businessId, @Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT si FROM SaleItem si WHERE si.sale.business.id = :businessId AND si.warrantyEndsAt BETWEEN :currentDate AND :expiryDate")
    List<SaleItem> findExpiringWarranties(@Param("businessId") Long businessId, 
                                         @Param("currentDate") LocalDateTime currentDate, 
                                         @Param("expiryDate") LocalDateTime expiryDate);
}
