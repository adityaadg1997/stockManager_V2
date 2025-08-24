package com.jmdt.stockmanager.service;

import com.jmdt.stockmanager.models.*;
import com.jmdt.stockmanager.enums.ReturnReason;
import com.jmdt.stockmanager.enums.ReturnStatus;
import com.jmdt.stockmanager.enums.WarrantyClaimStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for comprehensive customer management
 * Handles customer CRUD, loyalty programs, returns, warranties, and customer analytics
 */
public interface CustomerService {

    // ===== CUSTOMER MANAGEMENT =====
    
    /**
     * Create a new customer for a business
     */
    Customer createCustomer(Long businessId, Customer customer);
    
    /**
     * Create a new customer
     */
    Customer createCustomer(Customer customer);
    
    /**
     * Update customer details
     */
    Customer updateCustomer(Long customerId, Customer customer);
    
    /**
     * Get customer by ID
     */
    Optional<Customer> getCustomerById(Long customerId);
    
    /**
     * Get customer by phone number
     */
    Optional<Customer> getCustomerByPhone(String phone, Long businessId);
    
    /**
     * Get customer by phone number for business
     */
    Customer getCustomerByPhone(Long businessId, String phone);
    
    /**
     * Get customer by email
     */
    Optional<Customer> getCustomerByEmail(String email, Long businessId);
    
    /**
     * Get customer by email for business
     */
    Customer getCustomerByEmail(Long businessId, String email);
    
    /**
     * Get all customers for a business with pagination
     */
    Page<Customer> getCustomersByBusiness(Long businessId, Pageable pageable);
    
    /**
     * Search customers by name, phone, or email
     */
    Page<Customer> searchCustomers(Long businessId, String searchTerm, Pageable pageable);
    
    /**
     * Delete customer (soft delete)
     */
    void deleteCustomer(Long customerId);
    
    /**
     * Get customer count for a business
     */
    Long getCustomerCountByBusiness(Long businessId);
    
    // ===== LOYALTY PROGRAM MANAGEMENT =====
    
    /**
     * Add loyalty points to customer
     */
    Customer addLoyaltyPoints(Long customerId, Integer points, String reason);
    
    /**
     * Redeem loyalty points
     */
    Customer redeemLoyaltyPoints(Long customerId, Integer points, String reason);
    
    /**
     * Get customer loyalty points balance
     */
    Integer getLoyaltyPointsBalance(Long customerId);
    
    /**
     * Get customers with highest loyalty points
     */
    List<Customer> getTopLoyaltyCustomers(Long businessId, Integer limit);
    
    /**
     * Calculate loyalty points for purchase amount
     */
    Integer calculateLoyaltyPointsForPurchase(Double purchaseAmount);
    
    /**
     * Get loyalty points history for customer
     */
    List<Map<String, Object>> getLoyaltyPointsHistory(Long customerId);
    
    // ===== CUSTOMER RETURNS MANAGEMENT =====
    
    /**
     * Create a customer return request
     */
    CustomerReturn createCustomerReturn(CustomerReturn customerReturn);
    
    /**
     * Update return status
     */
    CustomerReturn updateReturnStatus(Long returnId, ReturnStatus status, String notes);
    
    /**
     * Get return by ID
     */
    Optional<CustomerReturn> getReturnById(Long returnId);
    
    /**
     * Get returns by customer
     */
    Page<CustomerReturn> getReturnsByCustomer(Long customerId, Pageable pageable);
    
    /**
     * Get returns by business
     */
    Page<CustomerReturn> getReturnsByBusiness(Long businessId, Pageable pageable);
    
    /**
     * Get returns by status
     */
    Page<CustomerReturn> getReturnsByStatus(Long businessId, ReturnStatus status, Pageable pageable);
    
    /**
     * Get returns by date range
     */
    Page<CustomerReturn> getReturnsByDateRange(Long businessId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Process return approval
     */
    CustomerReturn approveReturn(Long returnId, Long userId);
    
    /**
     * Process return rejection
     */
    CustomerReturn rejectReturn(Long returnId, String reason, Long userId);
    
    /**
     * Process return refund
     */
    CustomerReturn processReturnRefund(Long returnId, Double refundAmount, String refundMethod, Long userId);
    
    // ===== RETURN ITEMS MANAGEMENT =====
    
    /**
     * Add item to return
     */
    ReturnItem addReturnItem(Long returnId, ReturnItem returnItem);
    
    /**
     * Update return item condition
     */
    ReturnItem updateReturnItemCondition(Long returnItemId, String condition, String notes);
    
    /**
     * Mark return item as restocked
     */
    ReturnItem markReturnItemRestocked(Long returnItemId, Integer restoredQuantity);
    
    /**
     * Get return items by return ID
     */
    List<ReturnItem> getReturnItemsByReturn(Long returnId);
    
    // ===== WARRANTY CLAIMS MANAGEMENT =====
    
    /**
     * Create warranty claim
     */
    WarrantyClaim createWarrantyClaim(WarrantyClaim warrantyClaim);
    
    /**
     * Update warranty claim status
     */
    WarrantyClaim updateWarrantyClaimStatus(Long claimId, WarrantyClaimStatus status, String notes);
    
    /**
     * Get warranty claim by ID
     */
    Optional<WarrantyClaim> getWarrantyClaimById(Long claimId);
    
    /**
     * Get warranty claims by customer
     */
    Page<WarrantyClaim> getWarrantyClaimsByCustomer(Long customerId, Pageable pageable);
    
    /**
     * Get warranty claims by status
     */
    Page<WarrantyClaim> getWarrantyClaimsByStatus(Long businessId, WarrantyClaimStatus status, Pageable pageable);
    
    /**
     * Get active warranty claims
     */
    Page<WarrantyClaim> getActiveWarrantyClaims(Long businessId, Pageable pageable);
    
    /**
     * Check if product is under warranty for customer
     */
    boolean isProductUnderWarranty(Long saleItemId);
    
    /**
     * Get warranty expiry date for sale item
     */
    LocalDateTime getWarrantyExpiryDate(Long saleItemId);
    
    /**
     * Process warranty claim approval
     */
    WarrantyClaim approveWarrantyClaim(Long claimId, Long userId);
    
    /**
     * Process warranty claim rejection
     */
    WarrantyClaim rejectWarrantyClaim(Long claimId, String reason, Long userId);
    
    /**
     * Process warranty replacement
     */
    WarrantyClaim processWarrantyReplacement(Long claimId, Long replacementProductId, Long userId);
    
    // ===== CUSTOMER ANALYTICS =====
    
    /**
     * Get customer purchase history
     */
    Page<Sale> getCustomerPurchaseHistory(Long customerId, Pageable pageable);
    
    /**
     * Get customer total spent amount
     */
    Double getCustomerTotalSpent(Long customerId);
    
    /**
     * Get customer average order value
     */
    Double getCustomerAverageOrderValue(Long customerId);
    
    /**
     * Get customer purchase frequency (orders per month)
     */
    Double getCustomerPurchaseFrequency(Long customerId);
    
    /**
     * Get customer lifetime value
     */
    Double getCustomerLifetimeValue(Long customerId);
    
    /**
     * Get customer segmentation data
     */
    Map<String, Object> getCustomerSegmentation(Long customerId);
    
    /**
     * Get customer risk score (based on returns, complaints, etc.)
     */
    Double getCustomerRiskScore(Long customerId);
    
    /**
     * Get customer satisfaction score
     */
    Double getCustomerSatisfactionScore(Long customerId);
    
    // ===== CUSTOMER INSIGHTS =====
    
    /**
     * Get top customers by purchase amount
     */
    List<Customer> getTopCustomersByPurchaseAmount(Long businessId, Integer limit);
    
    /**
     * Get top customers by purchase frequency
     */
    List<Customer> getTopCustomersByPurchaseFrequency(Long businessId, Integer limit);
    
    /**
     * Get new customers for date range
     */
    List<Customer> getNewCustomers(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get inactive customers (no purchase for specified days)
     */
    List<Customer> getInactiveCustomers(Long businessId, Integer daysThreshold);
    
    /**
     * Get customers at risk of churning
     */
    List<Customer> getCustomersAtRiskOfChurning(Long businessId);
    
    /**
     * Get customers with high return rate
     */
    List<Customer> getCustomersWithHighReturnRate(Long businessId, Double returnRateThreshold);
    
    /**
     * Get customer demographics summary
     */
    Map<String, Object> getCustomerDemographics(Long businessId);
    
    // ===== CUSTOMER COMMUNICATION =====
    
    /**
     * Send communication to customer
     */
    CommunicationLog sendCommunication(Long customerId, String type, String message, Long userId);
    
    /**
     * Send bulk communication to customers
     */
    List<CommunicationLog> sendBulkCommunication(List<Long> customerIds, String type, String message, Long userId);
    
    /**
     * Get communication history for customer
     */
    Page<CommunicationLog> getCustomerCommunicationHistory(Long customerId, Pageable pageable);
    
    /**
     * Send return confirmation
     */
    void sendReturnConfirmation(Long returnId);
    
    /**
     * Send warranty claim update
     */
    void sendWarrantyClaimUpdate(Long claimId);
    
    /**
     * Send loyalty points update
     */
    void sendLoyaltyPointsUpdate(Long customerId, Integer pointsAdded, String reason);
    
    // ===== CUSTOMER REPORTS =====
    
    /**
     * Generate customer acquisition report
     */
    Map<String, Object> generateCustomerAcquisitionReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate customer retention report
     */
    Map<String, Object> generateCustomerRetentionReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate customer lifetime value report
     */
    Map<String, Object> generateCustomerLifetimeValueReport(Long businessId);
    
    /**
     * Generate returns analysis report
     */
    Map<String, Object> generateReturnsAnalysisReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate warranty claims report
     */
    Map<String, Object> generateWarrantyClaimsReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate loyalty program performance report
     */
    Map<String, Object> generateLoyaltyProgramReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    // ===== CUSTOMER PREFERENCES =====
    
    /**
     * Update customer communication preferences
     */
    Customer updateCommunicationPreferences(Long customerId, Map<String, Boolean> preferences);
    
    /**
     * Get customer preferred products
     */
    List<Product> getCustomerPreferredProducts(Long customerId);
    
    /**
     * Get customer purchase patterns
     */
    Map<String, Object> getCustomerPurchasePatterns(Long customerId);
    
    /**
     * Get recommended products for customer
     */
    List<Product> getRecommendedProducts(Long customerId, Integer limit);
    
    // ===== ADDITIONAL CONTROLLER METHODS =====
    
    /**
     * Filter customers by various criteria
     */
    Page<Customer> filterCustomers(Long businessId, String phone, String email, String gstin, 
                                 Integer minLoyaltyPoints, Integer maxLoyaltyPoints, Pageable pageable);
    
    /**
     * Get top customers by criteria
     */
    List<Customer> getTopCustomers(Long businessId, int limit, String criteria);
    
    /**
     * Get loyalty points distribution
     */
    Map<String, Object> getLoyaltyPointsDistribution(Long businessId);
    
    /**
     * Get customer segments
     */
    Map<String, Object> getCustomerSegments(Long businessId);
    
    /**
     * Get customer purchase history with date range
     */
    Map<String, Object> getCustomerPurchaseHistory(Long customerId, LocalDateTime startDate, 
                                                  LocalDateTime endDate, Pageable pageable);
    
    /**
     * Get customer spending summary
     */
    Map<String, Object> getCustomerSpendingSummary(Long customerId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Send SMS to customer
     */
    boolean sendSMS(Long customerId, String message, String templateType);
    
    /**
     * Send email to customer
     */
    boolean sendEmail(Long customerId, String subject, String message, String templateType);
    
    /**
     * Send WhatsApp message to customer
     */
    boolean sendWhatsApp(Long customerId, String message, String templateType);
    
    /**
     * Get communication history for customer
     */
    Map<String, Object> getCommunicationHistory(Long customerId, String type, Pageable pageable);
    
    /**
     * Bulk import customers
     */
    List<Customer> bulkImportCustomers(Long businessId, List<Customer> customers);
    
    /**
     * Bulk update loyalty points
     */
    int bulkUpdateLoyaltyPoints(Long businessId, List<Long> customerIds, Integer pointsToAdd, String reason);
    
    /**
     * Bulk send communication
     */
    Map<String, Object> bulkSendCommunication(Long businessId, List<Long> customerIds, 
                                            String type, String message, String subject);
    
    /**
     * Check if phone number exists
     */
    boolean phoneNumberExists(Long businessId, String phone);
    
    /**
     * Check if email address exists
     */
    boolean emailAddressExists(Long businessId, String email);
    
    /**
     * Export customers data
     */
    byte[] exportCustomers(Long businessId, String format, String filters);
}
