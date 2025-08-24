package com.jmdt.stockmanager.service;

import com.jmdt.stockmanager.models.Business;
import com.jmdt.stockmanager.enums.PlanType;

import java.util.List;
import java.util.Optional;

public interface BusinessService {
    
    Business createBusiness(Business business);
    
    Business updateBusiness(Long businessId, Business business);
    
    Optional<Business> getBusinessById(Long businessId);
    
    List<Business> getAllBusinesses();
    
    List<Business> getBusinessesByPlan(PlanType planType);
    
    void deleteBusiness(Long businessId);
    
    boolean existsByName(String name);
    
    boolean existsByContactEmail(String contactEmail);
}
