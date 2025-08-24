package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    List<Customer> findByBusinessId(Long businessId);
    
    Page<Customer> findByBusinessId(Long businessId, Pageable pageable);
    
    Optional<Customer> findByBusinessIdAndPhone(Long businessId, String phone);
    
    Optional<Customer> findByBusinessIdAndEmail(Long businessId, String email);
    
    // Additional methods for CustomerServiceImpl compatibility
    Optional<Customer> findByPhoneAndBusinessId(String phone, Long businessId);
    
    Optional<Customer> findByEmailAndBusinessId(String email, Long businessId);
    
    Long countByBusinessId(Long businessId);
    
    Optional<Customer> findByBusinessIdAndGstin(Long businessId, String gstin);
    
    @Query("SELECT c FROM Customer c WHERE c.business.id = :businessId AND c.name LIKE %:name%")
    List<Customer> findByBusinessIdAndNameContaining(@Param("businessId") Long businessId, @Param("name") String name);
    
    @Query("SELECT c FROM Customer c WHERE c.business.id = :businessId AND c.loyaltyPoints >= :minPoints ORDER BY c.loyaltyPoints DESC")
    List<Customer> findTopLoyaltyCustomers(@Param("businessId") Long businessId, @Param("minPoints") Integer minPoints);
}
