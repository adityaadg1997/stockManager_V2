package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    
    List<Warehouse> findByBusinessId(Long businessId);
    
    List<Warehouse> findByBusinessIdAndIsActive(Long businessId, Boolean isActive);
    
    Optional<Warehouse> findByBusinessIdAndName(Long businessId, String name);
}
