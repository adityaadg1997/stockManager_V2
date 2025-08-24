package com.jmdt.stockmanager.service.impl;

import com.jmdt.stockmanager.enums.PlanType;
import com.jmdt.stockmanager.exception.ResourceNotFoundException;
import com.jmdt.stockmanager.models.Business;
import com.jmdt.stockmanager.repository.BusinessRepository;
import com.jmdt.stockmanager.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Override
    public Business createBusiness(Business business) {
        log.info("Creating new business: {}", business.getName());
        business.setCreatedAt(LocalDateTime.now());
        business.setUpdatedAt(LocalDateTime.now());
        return businessRepository.save(business);
    }

    @Override
    @CachePut(value = "businesses", key = "#businessId")
    @CacheEvict(value = "businesses", key = "'all'")
    public Business updateBusiness(Long businessId, Business business) {
        log.info("Updating business with ID: {}", businessId);
        Business existingBusiness = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business", "id", businessId.toString()));
        
        existingBusiness.setName(business.getName());
        existingBusiness.setContactEmail(business.getContactEmail());
        existingBusiness.setPhone(business.getPhone());
        existingBusiness.setAddress(business.getAddress());
        existingBusiness.setLogoUrl(business.getLogoUrl());
        existingBusiness.setPlan(business.getPlan());
        existingBusiness.setTimezone(business.getTimezone());
        existingBusiness.setUpdatedAt(LocalDateTime.now());
        
        return businessRepository.save(existingBusiness);
    }

    @Override
    @Cacheable(value = "businesses", key = "#businessId")
    public Optional<Business> getBusinessById(Long businessId) {
        return businessRepository.findById(businessId);
    }

    @Override
    @Cacheable(value = "businesses", key = "'all'")
    public List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }

    @Override
    @Cacheable(value = "businesses", key = "'plan_' + #planType")
    public List<Business> getBusinessesByPlan(PlanType planType) {
        return businessRepository.findAll().stream()
                .filter(business -> business.getPlan().equals(planType))
                .toList();
    }

    @Override
    @CacheEvict(value = "businesses", allEntries = true)
    public void deleteBusiness(Long businessId) {
        log.info("Deleting business with ID: {}", businessId);
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business", "id", businessId.toString()));
        businessRepository.delete(business);
    }

    @Override
    public boolean existsByName(String name) {
        return businessRepository.findByName(name).isPresent();
    }

    @Override
    public boolean existsByContactEmail(String contactEmail) {
        return businessRepository.findByContactEmail(contactEmail).isPresent();
    }
}
