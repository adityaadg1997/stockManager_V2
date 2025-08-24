package com.jmdt.stockmanager.controller;

import com.jmdt.stockmanager.enums.SaleStatus;
import com.jmdt.stockmanager.models.Sale;
import com.jmdt.stockmanager.models.SaleItem;
import com.jmdt.stockmanager.models.Payment;
import com.jmdt.stockmanager.payloads.ApiResponse;
import com.jmdt.stockmanager.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SalesController {

    @Autowired
    private SalesService salesService;

    // ===== SALE CRUD OPERATIONS =====

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> createSale(@RequestBody Sale sale) {
        try {
            Sale createdSale = salesService.createSale(sale);
            ApiResponse response = new ApiResponse("Sale created successfully", true);
            response.setData(createdSale);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to create sale: " + e.getMessage(), false));
        }
    }

    @PutMapping("/{saleId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> updateSale(@PathVariable Long saleId, @RequestBody Sale sale) {
        try {
            Sale updatedSale = salesService.updateSale(saleId, sale);
            ApiResponse response = new ApiResponse("Sale updated successfully", true);
            response.setData(updatedSale);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to update sale: " + e.getMessage(), false));
        }
    }

    @GetMapping("/{saleId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getSaleById(@PathVariable Long saleId) {
        try {
            Optional<Sale> sale = salesService.getSaleById(saleId);
            if (sale.isPresent()) {
                ApiResponse response = new ApiResponse("Sale retrieved successfully", true);
                response.setData(sale.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to retrieve sale: " + e.getMessage(), false));
        }
    }

    @GetMapping("/business/{businessId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getSalesByBusiness(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            List<Sale> sales = salesService.getSalesByBusinessId(businessId);
            ApiResponse response = new ApiResponse("Sales retrieved successfully", true);
            response.setData(sales);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to retrieve sales: " + e.getMessage(), false));
        }
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getSalesByCustomer(@PathVariable Long customerId) {
        try {
            List<Sale> sales = salesService.getSalesByCustomerId(customerId);
            ApiResponse response = new ApiResponse("Customer sales retrieved successfully", true);
            response.setData(sales);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to retrieve customer sales: " + e.getMessage(), false));
        }
    }

    @DeleteMapping("/{saleId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> deleteSale(@PathVariable Long saleId) {
        try {
            salesService.deleteSale(saleId);
            return ResponseEntity.ok(new ApiResponse("Sale deleted successfully", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to delete sale: " + e.getMessage(), false));
        }
    }

    // ===== SALE SEARCH AND FILTERING =====

    @GetMapping("/business/{businessId}/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getSalesByDateRange(
            @PathVariable Long businessId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<Sale> sales = salesService.getSalesByDateRange(businessId, startDate, endDate);
            ApiResponse response = new ApiResponse("Sales retrieved successfully", true);
            response.setData(sales);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to retrieve sales: " + e.getMessage(), false));
        }
    }

    @GetMapping("/business/{businessId}/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getSalesByStatus(
            @PathVariable Long businessId,
            @PathVariable SaleStatus status) {
        try {
            List<Sale> sales = salesService.getSalesByStatus(businessId, status);
            ApiResponse response = new ApiResponse("Sales retrieved successfully", true);
            response.setData(sales);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to retrieve sales: " + e.getMessage(), false));
        }
    }

    @GetMapping("/invoice/{invoiceNo}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getSaleByInvoiceNo(@PathVariable String invoiceNo) {
        try {
            Optional<Sale> sale = salesService.getSaleByInvoiceNo(invoiceNo);
            if (sale.isPresent()) {
                ApiResponse response = new ApiResponse("Sale retrieved successfully", true);
                response.setData(sale.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to retrieve sale: " + e.getMessage(), false));
        }
    }

    // ===== SALE ITEMS MANAGEMENT =====

    @PostMapping("/{saleId}/items")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> addSaleItem(@PathVariable Long saleId, @RequestBody SaleItem saleItem) {
        try {
            SaleItem addedItem = salesService.addSaleItem(saleId, saleItem);
            ApiResponse response = new ApiResponse("Sale item added successfully", true);
            response.setData(addedItem);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to add sale item: " + e.getMessage(), false));
        }
    }

    @GetMapping("/{saleId}/items")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getSaleItems(@PathVariable Long saleId) {
        try {
            List<SaleItem> items = salesService.getSaleItemsBySaleId(saleId);
            ApiResponse response = new ApiResponse("Sale items retrieved successfully", true);
            response.setData(items);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to retrieve sale items: " + e.getMessage(), false));
        }
    }

    @PutMapping("/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> updateSaleItem(@PathVariable Long itemId, @RequestBody SaleItem saleItem) {
        try {
            SaleItem updatedItem = salesService.updateSaleItem(itemId, saleItem);
            ApiResponse response = new ApiResponse("Sale item updated successfully", true);
            response.setData(updatedItem);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to update sale item: " + e.getMessage(), false));
        }
    }

    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> removeSaleItem(@PathVariable Long itemId) {
        try {
            salesService.removeSaleItem(itemId);
            return ResponseEntity.ok(new ApiResponse("Sale item removed successfully", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to remove sale item: " + e.getMessage(), false));
        }
    }

    // ===== PAYMENT MANAGEMENT =====

    @PostMapping("/{saleId}/payments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> addPayment(@PathVariable Long saleId, @RequestBody Payment payment) {
        try {
            Payment addedPayment = salesService.addPayment(saleId, payment);
            ApiResponse response = new ApiResponse("Payment added successfully", true);
            response.setData(addedPayment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to add payment: " + e.getMessage(), false));
        }
    }

    @GetMapping("/{saleId}/payments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getSalePayments(@PathVariable Long saleId) {
        try {
            List<Payment> payments = salesService.getPaymentsBySaleId(saleId);
            ApiResponse response = new ApiResponse("Payments retrieved successfully", true);
            response.setData(payments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to retrieve payments: " + e.getMessage(), false));
        }
    }

    @PostMapping("/payments/{paymentId}/refund")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> processRefund(
            @PathVariable Long paymentId,
            @RequestParam BigDecimal refundAmount,
            @RequestParam String reason) {
        try {
            Payment refund = salesService.processRefund(paymentId, refundAmount, reason);
            ApiResponse response = new ApiResponse("Refund processed successfully", true);
            response.setData(refund);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to process refund: " + e.getMessage(), false));
        }
    }

    // ===== BUSINESS LOGIC OPERATIONS =====

    @PostMapping("/{saleId}/complete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> completeSale(@PathVariable Long saleId) {
        try {
            Sale completedSale = salesService.completeSale(saleId);
            ApiResponse response = new ApiResponse("Sale completed successfully", true);
            response.setData(completedSale);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to complete sale: " + e.getMessage(), false));
        }
    }

    @PostMapping("/{saleId}/cancel")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> cancelSale(@PathVariable Long saleId, @RequestParam String reason) {
        try {
            Sale cancelledSale = salesService.cancelSale(saleId, reason);
            ApiResponse response = new ApiResponse("Sale cancelled successfully", true);
            response.setData(cancelledSale);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to cancel sale: " + e.getMessage(), false));
        }
    }

    @GetMapping("/{saleId}/total")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> calculateSaleTotal(@PathVariable Long saleId) {
        try {
            BigDecimal total = salesService.calculateSaleTotal(saleId);
            ApiResponse response = new ApiResponse("Sale total calculated successfully", true);
            response.setData(total);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to calculate sale total: " + e.getMessage(), false));
        }
    }

    @GetMapping("/{saleId}/outstanding")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getOutstandingAmount(@PathVariable Long saleId) {
        try {
            BigDecimal outstanding = salesService.calculateOutstandingAmount(saleId);
            ApiResponse response = new ApiResponse("Outstanding amount retrieved successfully", true);
            response.setData(outstanding);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to get outstanding amount: " + e.getMessage(), false));
        }
    }

    @GetMapping("/{saleId}/fully-paid")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> isSaleFullyPaid(@PathVariable Long saleId) {
        try {
            boolean fullyPaid = salesService.isSaleFullyPaid(saleId);
            ApiResponse response = new ApiResponse("Payment status retrieved successfully", true);
            response.setData(fullyPaid);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to check payment status: " + e.getMessage(), false));
        }
    }

    // ===== REPORTING AND ANALYTICS =====

    @GetMapping("/business/{businessId}/analytics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getSalesAnalytics(
            @PathVariable Long businessId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            BigDecimal totalAmount = salesService.getTotalSalesAmount(businessId, startDate, endDate);
            Long totalCount = salesService.getTotalSalesCount(businessId, startDate, endDate);
            BigDecimal avgOrderValue = salesService.getAverageOrderValue(businessId, startDate, endDate);
            
            Map<String, Object> analytics = Map.of(
                "totalAmount", totalAmount,
                "totalCount", totalCount,
                "averageOrderValue", avgOrderValue,
                "period", startDate + " to " + endDate
            );
            
            ApiResponse response = new ApiResponse("Sales analytics retrieved successfully", true);
            response.setData(analytics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to retrieve analytics: " + e.getMessage(), false));
        }
    }

    @GetMapping("/business/{businessId}/top-sales")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getTopSales(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<Sale> topSales = salesService.getTopSalesByAmount(businessId, limit);
            ApiResponse response = new ApiResponse("Top sales retrieved successfully", true);
            response.setData(topSales);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to retrieve top sales: " + e.getMessage(), false));
        }
    }

    // ===== INVOICE MANAGEMENT =====

    @PostMapping("/{saleId}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> updateSaleStatus(@PathVariable Long saleId, @RequestParam SaleStatus status) {
        try {
            salesService.updateSaleStatus(saleId, status);
            return ResponseEntity.ok(new ApiResponse("Sale status updated successfully", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to update sale status: " + e.getMessage(), false));
        }
    }

    // ===== WARRANTY MANAGEMENT =====

    @PostMapping("/{saleId}/warranty/calculate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> calculateWarrantyEndDates(@PathVariable Long saleId) {
        try {
            salesService.calculateWarrantyEndDates(saleId);
            return ResponseEntity.ok(new ApiResponse("Warranty end dates calculated successfully", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to calculate warranty dates: " + e.getMessage(), false));
        }
    }

    @GetMapping("/business/{businessId}/warranty/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getItemsUnderWarranty(@PathVariable Long businessId) {
        try {
            List<SaleItem> items = salesService.getItemsUnderWarranty(businessId);
            ApiResponse response = new ApiResponse("Items under warranty retrieved successfully", true);
            response.setData(items);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to retrieve warranty items: " + e.getMessage(), false));
        }
    }

    @GetMapping("/business/{businessId}/warranty/expiring")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getExpiringWarranties(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "30") int daysFromNow) {
        try {
            List<SaleItem> items = salesService.getExpiringWarranties(businessId, daysFromNow);
            ApiResponse response = new ApiResponse("Expiring warranties retrieved successfully", true);
            response.setData(items);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Failed to retrieve expiring warranties: " + e.getMessage(), false));
        }
    }
}
