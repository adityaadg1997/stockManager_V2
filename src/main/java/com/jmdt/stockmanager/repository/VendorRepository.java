package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    
    List<Vendor> findByBusinessId(Long businessId);
    
    Optional<Vendor> findByBusinessIdAndName(Long businessId, String name);
    
    Optional<Vendor> findByBusinessIdAndContactEmail(Long businessId, String contactEmail);
    
    @Query("SELECT v FROM Vendor v WHERE v.business.id = :businessId AND v.name LIKE %:name%")
    List<Vendor> findByBusinessIdAndNameContaining(@Param("businessId") Long businessId, @Param("name") String name);
}
