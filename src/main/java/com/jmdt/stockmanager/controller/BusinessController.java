package com.jmdt.stockmanager.controller;

import com.jmdt.stockmanager.enums.PlanType;
import com.jmdt.stockmanager.models.Business;
import com.jmdt.stockmanager.payloads.ApiResponse;
import com.jmdt.stockmanager.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/businesses")
@CrossOrigin(origins = "*")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @PostMapping
    public ResponseEntity<Business> createBusiness(@Valid @RequestBody Business business) {
        log.info("Creating new business: {}", business.getName());
        
        if (businessService.existsByName(business.getName())) {
            return ResponseEntity.badRequest().build();
        }
        
        if (business.getContactEmail() != null && businessService.existsByContactEmail(business.getContactEmail())) {
            return ResponseEntity.badRequest().build();
        }
        
        Business createdBusiness = businessService.createBusiness(business);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBusiness);
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<Business> getBusinessById(@PathVariable Long businessId) {
        Optional<Business> business = businessService.getBusinessById(businessId);
        return business.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Business>> getAllBusinesses() {
        List<Business> businesses = businessService.getAllBusinesses();
        return ResponseEntity.ok(businesses);
    }

    @GetMapping("/plan/{planType}")
    public ResponseEntity<List<Business>> getBusinessesByPlan(@PathVariable PlanType planType) {
        List<Business> businesses = businessService.getBusinessesByPlan(planType);
        return ResponseEntity.ok(businesses);
    }

    @PutMapping("/{businessId}")
    public ResponseEntity<Business> updateBusiness(@PathVariable Long businessId, 
                                                  @Valid @RequestBody Business business) {
        log.info("Updating business with ID: {}", businessId);
        Business updatedBusiness = businessService.updateBusiness(businessId, business);
        return ResponseEntity.ok(updatedBusiness);
    }

    @DeleteMapping("/{businessId}")
    public ResponseEntity<ApiResponse> deleteBusiness(@PathVariable Long businessId) {
        log.info("Deleting business with ID: {}", businessId);
        businessService.deleteBusiness(businessId);
        return ResponseEntity.ok(new ApiResponse("Business deleted successfully", true));
    }

    @GetMapping("/check-name/{name}")
    public ResponseEntity<ApiResponse> checkBusinessNameExists(@PathVariable String name) {
        boolean exists = businessService.existsByName(name);
        return ResponseEntity.ok(new ApiResponse("Name availability checked", !exists));
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<ApiResponse> checkBusinessEmailExists(@PathVariable String email) {
        boolean exists = businessService.existsByContactEmail(email);
        return ResponseEntity.ok(new ApiResponse("Email availability checked", !exists));
    }
}
