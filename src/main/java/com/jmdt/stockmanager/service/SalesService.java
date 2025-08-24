package com.jmdt.stockmanager.service;

import com.jmdt.stockmanager.models.Sale;
import com.jmdt.stockmanager.models.SaleItem;
import com.jmdt.stockmanager.models.Payment;
import com.jmdt.stockmanager.enums.SaleStatus;
import com.jmdt.stockmanager.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SalesService {
    
    // Sale CRUD operations
    Sale createSale(Sale sale);
    Sale updateSale(Long saleId, Sale sale);
    Optional<Sale> getSaleById(Long saleId);
    List<Sale> getSalesByBusinessId(Long businessId);
    List<Sale> getSalesByCustomerId(Long customerId);
    void deleteSale(Long saleId);
    
    // Sale search and filtering
    List<Sale> getSalesByDateRange(Long businessId, LocalDateTime startDate, LocalDateTime endDate);
    List<Sale> getSalesByStatus(Long businessId, SaleStatus status);
    List<Sale> getSalesByUser(Long businessId, Long userId);
    Optional<Sale> getSaleByInvoiceNo(String invoiceNo);
    
    // Sale items management
    SaleItem addSaleItem(Long saleId, SaleItem saleItem);
    List<SaleItem> getSaleItemsBySaleId(Long saleId);
    SaleItem updateSaleItem(Long saleItemId, SaleItem saleItem);
    void removeSaleItem(Long saleItemId);
    
    // Payment management
    Payment addPayment(Long saleId, Payment payment);
    List<Payment> getPaymentsBySaleId(Long saleId);
    List<Payment> getPaymentsByCustomerId(Long customerId);
    Payment processRefund(Long paymentId, BigDecimal refundAmount, String reason);
    
    // Business logic
    Sale completeSale(Long saleId);
    Sale cancelSale(Long saleId, String reason);
    BigDecimal calculateSaleTotal(Long saleId);
    BigDecimal calculateOutstandingAmount(Long saleId);
    boolean isSaleFullyPaid(Long saleId);
    
    // Reporting
    BigDecimal getTotalSalesAmount(Long businessId, LocalDateTime startDate, LocalDateTime endDate);
    Long getTotalSalesCount(Long businessId, LocalDateTime startDate, LocalDateTime endDate);
    BigDecimal getAverageOrderValue(Long businessId, LocalDateTime startDate, LocalDateTime endDate);
    List<Sale> getTopSalesByAmount(Long businessId, int limit);
    
    // Invoice management
    String generateInvoiceNumber(Long businessId);
    void updateSaleStatus(Long saleId, SaleStatus status);
    
    // Warranty management
    void calculateWarrantyEndDates(Long saleId);
    List<SaleItem> getItemsUnderWarranty(Long businessId);
    List<SaleItem> getExpiringWarranties(Long businessId, int daysFromNow);
}
