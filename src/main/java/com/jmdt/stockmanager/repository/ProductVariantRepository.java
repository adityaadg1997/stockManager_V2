package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    
    List<ProductVariant> findByProductId(Long productId);
    
    boolean existsById(Long variantId);
    
    void deleteById(Long variantId);
}
