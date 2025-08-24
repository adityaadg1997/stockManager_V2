package com.jmdt.stockmanager.service.impl;

import com.jmdt.stockmanager.enums.PaymentMethod;
import com.jmdt.stockmanager.enums.SaleStatus;
import com.jmdt.stockmanager.exception.ResourceNotFoundException;
import com.jmdt.stockmanager.models.*;
import com.jmdt.stockmanager.repository.*;
import com.jmdt.stockmanager.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SalesServiceImpl implements SalesService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    // Sale CRUD operations
    @Override
    public Sale createSale(Sale sale) {
        validateSale(sale);
        
        // Generate invoice number if not provided
        if (sale.getInvoiceNo() == null || sale.getInvoiceNo().isEmpty()) {
            sale.setInvoiceNo(generateInvoiceNumber(sale.getBusiness().getId()));
        }
        
        // Set default status
        if (sale.getStatus() == null) {
            sale.setStatus(SaleStatus.COMPLETED);
        }
        
        // Set sale date if not provided
        if (sale.getSaleDate() == null) {
            sale.setSaleDate(LocalDateTime.now());
        }
        
        return saleRepository.save(sale);
    }

    @Override
    public Sale updateSale(Long saleId, Sale saleDetails) {
        Sale existingSale = getSaleById(saleId).orElseThrow(() -> 
            new ResourceNotFoundException("Sale not found with id: " + saleId));
        
        // Update allowed fields
        if (saleDetails.getCustomer() != null) {
            existingSale.setCustomer(saleDetails.getCustomer());
        }
        existingSale.setTotalAmount(saleDetails.getTotalAmount());
        existingSale.setDiscountAmount(saleDetails.getDiscountAmount());
        existingSale.setTaxAmount(saleDetails.getTaxAmount());
        if (saleDetails.getStatus() != null) {
            existingSale.setStatus(saleDetails.getStatus());
        }
        
        return saleRepository.save(existingSale);
    }

    @Override
    public Optional<Sale> getSaleById(Long saleId) {
        return saleRepository.findById(saleId);
    }

    @Override
    public List<Sale> getSalesByBusinessId(Long businessId) {
        return saleRepository.findByBusinessIdOrderByCreatedAtDesc(businessId);
    }

    @Override
    public List<Sale> getSalesByCustomerId(Long customerId) {
        return saleRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    @Override
    public void deleteSale(Long saleId) {
        Sale sale = getSaleById(saleId).orElseThrow(() -> 
            new ResourceNotFoundException("Sale not found with id: " + saleId));
        
        // Check if sale can be deleted (business logic)
        if (sale.getStatus() == SaleStatus.COMPLETED && hasPendingReturns(saleId)) {
            throw new IllegalStateException("Cannot delete sale with pending returns");
        }
        
        saleRepository.delete(sale);
    }

    // Sale search and filtering
    @Override
    public List<Sale> getSalesByDateRange(Long businessId, LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findByBusinessIdAndSaleDateBetweenOrderByCreatedAtDesc(businessId, startDate, endDate);
    }

    @Override
    public List<Sale> getSalesByStatus(Long businessId, SaleStatus status) {
        return saleRepository.findByBusinessIdAndStatusOrderByCreatedAtDesc(businessId, status);
    }

    @Override
    public List<Sale> getSalesByUser(Long businessId, Long userId) {
        return saleRepository.findByBusinessIdAndCreatedBy(businessId, userId);
    }

    @Override
    public Optional<Sale> getSaleByInvoiceNo(String invoiceNo) {
        return saleRepository.findByInvoiceNo(invoiceNo);
    }

    // Sale items management
    @Override
    public SaleItem addSaleItem(Long saleId, SaleItem saleItem) {
        Sale sale = getSaleById(saleId).orElseThrow(() -> 
            new ResourceNotFoundException("Sale not found with id: " + saleId));
        
        saleItem.setSale(sale);
        
        // Calculate warranty end date if product has warranty
        if (saleItem.getProduct() != null && saleItem.getProduct().getHasWarranty()) {
            LocalDateTime warrantyEnd = LocalDateTime.now()
                .plusDays(saleItem.getProduct().getWarrantyDurationDays());
            saleItem.setWarrantyEndsAt(warrantyEnd);
        }
        
        return saleItemRepository.save(saleItem);
    }

    @Override
    public List<SaleItem> getSaleItemsBySaleId(Long saleId) {
        return saleItemRepository.findBySaleId(saleId);
    }

    @Override
    public SaleItem updateSaleItem(Long saleItemId, SaleItem saleItem) {
        SaleItem existingItem = saleItemRepository.findById(saleItemId)
            .orElseThrow(() -> new ResourceNotFoundException("Sale item not found with id: " + saleItemId));
        
        existingItem.setQuantity(saleItem.getQuantity());
        existingItem.setPriceEach(saleItem.getPriceEach());
        existingItem.setDiscount(saleItem.getDiscount());
        existingItem.setTaxApplied(saleItem.getTaxApplied());
        
        return saleItemRepository.save(existingItem);
    }

    @Override
    public void removeSaleItem(Long saleItemId) {
        SaleItem saleItem = saleItemRepository.findById(saleItemId)
            .orElseThrow(() -> new ResourceNotFoundException("Sale item not found with id: " + saleItemId));
        
        saleItemRepository.delete(saleItem);
    }

    // Payment management
    @Override
    public Payment addPayment(Long saleId, Payment payment) {
        Sale sale = getSaleById(saleId).orElseThrow(() -> 
            new ResourceNotFoundException("Sale not found with id: " + saleId));
        
        payment.setSale(sale);
        
        // Set payment date if not provided
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }
        
        // Business logic for payment processing
        BigDecimal totalPaid = paymentRepository.getTotalPaidAmountBySaleId(saleId);
        BigDecimal newTotal = totalPaid.add(payment.getAmountPaid());
        
        if (newTotal.compareTo(sale.getTotalAmount()) > 0) {
            throw new IllegalArgumentException("Payment amount exceeds sale total");
        }
        
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getPaymentsBySaleId(Long saleId) {
        return paymentRepository.findBySaleIdOrderByPaymentDateDesc(saleId);
    }

    @Override
    public List<Payment> getPaymentsByCustomerId(Long customerId) {
        return paymentRepository.findByCustomerIdOrderByPaymentDateDesc(customerId);
    }

    @Override
    public Payment processRefund(Long paymentId, BigDecimal refundAmount, String reason) {
        Payment originalPayment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
        
        if (refundAmount.compareTo(originalPayment.getAmountPaid()) > 0) {
            throw new IllegalArgumentException("Refund amount cannot exceed original payment");
        }
        
        Payment refund = new Payment();
        refund.setSale(originalPayment.getSale());
        refund.setCustomer(originalPayment.getCustomer());
        refund.setAmountPaid(refundAmount.negate()); // Negative amount for refund
        refund.setPaymentMethod(originalPayment.getPaymentMethod());
        refund.setPaymentDate(LocalDateTime.now());
        refund.setIsRefund(true);
        refund.setRefundForPayment(originalPayment);
        refund.setNotes(reason);
        
        return paymentRepository.save(refund);
    }

    // Business logic
    @Override
    public Sale completeSale(Long saleId) {
        Sale sale = getSaleById(saleId).orElseThrow(() -> 
            new ResourceNotFoundException("Sale not found with id: " + saleId));
        
        sale.setStatus(SaleStatus.COMPLETED);
        return saleRepository.save(sale);
    }

    @Override
    public Sale cancelSale(Long saleId, String reason) {
        Sale sale = getSaleById(saleId).orElseThrow(() -> 
            new ResourceNotFoundException("Sale not found with id: " + saleId));
        
        sale.setStatus(SaleStatus.CANCELLED);
        return saleRepository.save(sale);
    }

    @Override
    public BigDecimal calculateSaleTotal(Long saleId) {
        List<SaleItem> items = getSaleItemsBySaleId(saleId);
        return items.stream()
            .map(item -> {
                BigDecimal itemTotal = item.getPriceEach().multiply(BigDecimal.valueOf(item.getQuantity()));
                itemTotal = itemTotal.subtract(item.getDiscount());
                itemTotal = itemTotal.add(item.getTaxApplied());
                return itemTotal;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateOutstandingAmount(Long saleId) {
        Sale sale = getSaleById(saleId).orElseThrow(() -> 
            new ResourceNotFoundException("Sale not found with id: " + saleId));
        
        BigDecimal totalPaid = paymentRepository.getTotalPaidAmountBySaleId(saleId);
        return sale.getTotalAmount().subtract(totalPaid);
    }

    @Override
    public boolean isSaleFullyPaid(Long saleId) {
        BigDecimal outstanding = calculateOutstandingAmount(saleId);
        return outstanding.compareTo(BigDecimal.ZERO) <= 0;
    }

    // Reporting
    @Override
    public BigDecimal getTotalSalesAmount(Long businessId, LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.getTotalSalesAmount(businessId, startDate, endDate);
    }

    @Override
    public Long getTotalSalesCount(Long businessId, LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.getTotalSalesCount(businessId, startDate, endDate);
    }

    @Override
    public BigDecimal getAverageOrderValue(Long businessId, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal totalAmount = getTotalSalesAmount(businessId, startDate, endDate);
        Long totalCount = getTotalSalesCount(businessId, startDate, endDate);
        
        if (totalCount == 0) {
            return BigDecimal.ZERO;
        }
        
        return totalAmount.divide(BigDecimal.valueOf(totalCount), 2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public List<Sale> getTopSalesByAmount(Long businessId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return saleRepository.findTopSalesByAmount(businessId, pageable);
    }

    // Invoice management
    @Override
    public String generateInvoiceNumber(Long businessId) {
        String prefix = "INV-" + businessId + "-";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp;
    }

    @Override
    public void updateSaleStatus(Long saleId, SaleStatus status) {
        Sale sale = getSaleById(saleId).orElseThrow(() -> 
            new ResourceNotFoundException("Sale not found with id: " + saleId));
        
        sale.setStatus(status);
        saleRepository.save(sale);
    }

    // Warranty management
    @Override
    public void calculateWarrantyEndDates(Long saleId) {
        List<SaleItem> items = getSaleItemsBySaleId(saleId);
        
        for (SaleItem item : items) {
            if (item.getProduct() != null && item.getProduct().getHasWarranty()) {
                LocalDateTime warrantyEnd = LocalDateTime.now()
                    .plusDays(item.getProduct().getWarrantyDurationDays());
                item.setWarrantyEndsAt(warrantyEnd);
                saleItemRepository.save(item);
            }
        }
    }

    @Override
    public List<SaleItem> getItemsUnderWarranty(Long businessId) {
        return saleItemRepository.findItemsUnderWarranty(businessId, LocalDateTime.now());
    }

    @Override
    public List<SaleItem> getExpiringWarranties(Long businessId, int daysFromNow) {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime expiryDate = currentDate.plusDays(daysFromNow);
        return saleItemRepository.findExpiringWarranties(businessId, currentDate, expiryDate);
    }

    // Private helper methods
    private void validateSale(Sale sale) {
        if (sale.getBusiness() == null) {
            throw new IllegalArgumentException("Business is required");
        }
        
        if (sale.getTotalAmount() == null || sale.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total amount must be non-negative");
        }
        
        // Verify business exists
        businessRepository.findById(sale.getBusiness().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        
        // Verify customer exists if provided
        if (sale.getCustomer() != null) {
            customerRepository.findById(sale.getCustomer().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        }
    }
    
    private boolean hasPendingReturns(Long saleId) {
        // Check if there are any pending returns for this sale
        // This would require CustomerReturn repository which we'll implement later
        return false; // Placeholder implementation
    }
}
