package com.jmdt.stockmanager.controller;

import com.jmdt.stockmanager.exception.ResourceNotFoundException;
import com.jmdt.stockmanager.models.Customer;
import com.jmdt.stockmanager.payloads.ApiResponse;
import com.jmdt.stockmanager.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // ==================== BASIC CRUD OPERATIONS ====================

    @PostMapping("/business/{businessId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> createCustomer(
            @PathVariable Long businessId,
            @Valid @RequestBody Customer customer) {
        try {
            Customer createdCustomer = customerService.createCustomer(businessId, customer);
            ApiResponse response = new ApiResponse("Customer created successfully", true);
            response.setData(createdCustomer);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to create customer: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getCustomerById(@PathVariable Long customerId) {
        try {
            Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
            ApiResponse response = new ApiResponse("Customer retrieved successfully", true);
            response.setData(customer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Customer not found: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/business/{businessId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getCustomersByBusiness(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Customer> customers = customerService.getCustomersByBusiness(businessId, pageable);
            ApiResponse response = new ApiResponse("Customers retrieved successfully", true);
            response.setData(customers);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve customers: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> updateCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody Customer customerDetails) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(customerId, customerDetails);
            ApiResponse response = new ApiResponse("Customer updated successfully", true);
            response.setData(updatedCustomer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to update customer: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable Long customerId) {
        try {
            customerService.deleteCustomer(customerId);
            ApiResponse response = new ApiResponse("Customer deleted successfully", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to delete customer: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== SEARCH AND FILTER OPERATIONS ====================

    @GetMapping("/business/{businessId}/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> searchCustomers(
            @PathVariable Long businessId,
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Customer> customers = customerService.searchCustomers(businessId, query, pageable);
            ApiResponse response = new ApiResponse("Search completed successfully", true);
            response.setData(customers);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Search failed: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/filter")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> filterCustomers(
            @PathVariable Long businessId,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String gstin,
            @RequestParam(required = false) Integer minLoyaltyPoints,
            @RequestParam(required = false) Integer maxLoyaltyPoints,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Customer> customers = customerService.filterCustomers(
                businessId, phone, email, gstin, minLoyaltyPoints, maxLoyaltyPoints, pageable);
            ApiResponse response = new ApiResponse("Customers filtered successfully", true);
            response.setData(customers);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Filter failed: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/by-phone/{phone}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getCustomerByPhone(
            @PathVariable Long businessId,
            @PathVariable String phone) {
        try {
            Customer customer = customerService.getCustomerByPhone(businessId, phone);
            ApiResponse response = new ApiResponse("Customer found successfully", true);
            response.setData(customer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Customer not found: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/business/{businessId}/by-email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getCustomerByEmail(
            @PathVariable Long businessId,
            @PathVariable String email) {
        try {
            Customer customer = customerService.getCustomerByEmail(businessId, email);
            ApiResponse response = new ApiResponse("Customer found successfully", true);
            response.setData(customer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Customer not found: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // ==================== LOYALTY POINTS MANAGEMENT ====================

    @PostMapping("/{customerId}/loyalty/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> addLoyaltyPoints(
            @PathVariable Long customerId,
            @RequestParam Integer points,
            @RequestParam(required = false) String reason) {
        try {
            Customer customer = customerService.addLoyaltyPoints(customerId, points, reason);
            ApiResponse response = new ApiResponse("Loyalty points added successfully", true);
            response.setData(customer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to add loyalty points: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{customerId}/loyalty/redeem")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> redeemLoyaltyPoints(
            @PathVariable Long customerId,
            @RequestParam Integer points,
            @RequestParam(required = false) String reason) {
        try {
            Customer customer = customerService.redeemLoyaltyPoints(customerId, points, reason);
            ApiResponse response = new ApiResponse("Loyalty points redeemed successfully", true);
            response.setData(customer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to redeem loyalty points: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{customerId}/loyalty/history")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getLoyaltyPointsHistory(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            // This would require a LoyaltyTransaction entity - for now return empty
            ApiResponse response = new ApiResponse("Loyalty points history retrieved successfully", true);
            response.setData(List.of());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve loyalty history: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== CUSTOMER ANALYTICS ====================

    @GetMapping("/business/{businessId}/analytics/top-customers")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getTopCustomers(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "total_spent") String criteria) {
        try {
            List<Customer> topCustomers = customerService.getTopCustomers(businessId, limit, criteria);
            ApiResponse response = new ApiResponse("Top customers retrieved successfully", true);
            response.setData(topCustomers);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve top customers: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/analytics/loyalty-distribution")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getLoyaltyPointsDistribution(@PathVariable Long businessId) {
        try {
            Map<String, Object> distribution = customerService.getLoyaltyPointsDistribution(businessId);
            ApiResponse response = new ApiResponse("Loyalty points distribution retrieved successfully", true);
            response.setData(distribution);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve loyalty distribution: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/analytics/customer-segments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getCustomerSegments(@PathVariable Long businessId) {
        try {
            Map<String, Object> segments = customerService.getCustomerSegments(businessId);
            ApiResponse response = new ApiResponse("Customer segments retrieved successfully", true);
            response.setData(segments);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve customer segments: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{customerId}/analytics/purchase-history")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getCustomerPurchaseHistory(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Map<String, Object> purchaseHistory = customerService.getCustomerPurchaseHistory(
                customerId, startDate, endDate, pageable);
            ApiResponse response = new ApiResponse("Purchase history retrieved successfully", true);
            response.setData(purchaseHistory);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve purchase history: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{customerId}/analytics/spending-summary")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getCustomerSpendingSummary(
            @PathVariable Long customerId,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        try {
            Map<String, Object> spendingSummary = customerService.getCustomerSpendingSummary(
                customerId, startDate, endDate);
            ApiResponse response = new ApiResponse("Spending summary retrieved successfully", true);
            response.setData(spendingSummary);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve spending summary: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== CUSTOMER COMMUNICATION ====================

    @PostMapping("/{customerId}/send-sms")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> sendSMS(
            @PathVariable Long customerId,
            @RequestParam String message,
            @RequestParam(required = false) String templateType) {
        try {
            boolean sent = customerService.sendSMS(customerId, message, templateType);
            ApiResponse response = new ApiResponse(
                sent ? "SMS sent successfully" : "Failed to send SMS", sent);
            return new ResponseEntity<>(response, sent ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to send SMS: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{customerId}/send-email")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> sendEmail(
            @PathVariable Long customerId,
            @RequestParam String subject,
            @RequestParam String message,
            @RequestParam(required = false) String templateType) {
        try {
            boolean sent = customerService.sendEmail(customerId, subject, message, templateType);
            ApiResponse response = new ApiResponse(
                sent ? "Email sent successfully" : "Failed to send email", sent);
            return new ResponseEntity<>(response, sent ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to send email: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{customerId}/send-whatsapp")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> sendWhatsApp(
            @PathVariable Long customerId,
            @RequestParam String message,
            @RequestParam(required = false) String templateType) {
        try {
            boolean sent = customerService.sendWhatsApp(customerId, message, templateType);
            ApiResponse response = new ApiResponse(
                sent ? "WhatsApp message sent successfully" : "Failed to send WhatsApp message", sent);
            return new ResponseEntity<>(response, sent ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to send WhatsApp message: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{customerId}/communication-history")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getCommunicationHistory(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Map<String, Object> communicationHistory = customerService.getCommunicationHistory(
                customerId, type, pageable);
            ApiResponse response = new ApiResponse("Communication history retrieved successfully", true);
            response.setData(communicationHistory);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve communication history: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== BULK OPERATIONS ====================

    @PostMapping("/business/{businessId}/bulk-import")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> bulkImportCustomers(
            @PathVariable Long businessId,
            @RequestBody List<Customer> customers) {
        try {
            List<Customer> importedCustomers = customerService.bulkImportCustomers(businessId, customers);
            ApiResponse response = new ApiResponse("Customers imported successfully", true);
            response.setData(Map.of(
                "imported_count", importedCustomers.size(),
                "customers", importedCustomers
            ));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Bulk import failed: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/business/{businessId}/bulk-update-loyalty")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> bulkUpdateLoyaltyPoints(
            @PathVariable Long businessId,
            @RequestParam Integer pointsToAdd,
            @RequestParam(required = false) String reason,
            @RequestBody(required = false) List<Long> customerIds) {
        try {
            int updatedCount = customerService.bulkUpdateLoyaltyPoints(businessId, customerIds, pointsToAdd, reason);
            ApiResponse response = new ApiResponse("Loyalty points updated successfully", true);
            response.setData(Map.of("updated_count", updatedCount));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Bulk loyalty update failed: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/business/{businessId}/bulk-communication")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> bulkSendCommunication(
            @PathVariable Long businessId,
            @RequestParam String type, // sms, email, whatsapp
            @RequestParam String message,
            @RequestParam(required = false) String subject, // for email
            @RequestBody(required = false) List<Long> customerIds) {
        try {
            Map<String, Object> result = customerService.bulkSendCommunication(
                businessId, customerIds, type, message, subject);
            ApiResponse response = new ApiResponse("Bulk communication sent successfully", true);
            response.setData(result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Bulk communication failed: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== CUSTOMER VALIDATION ====================

    @GetMapping("/business/{businessId}/validate-phone/{phone}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> validatePhoneNumber(
            @PathVariable Long businessId,
            @PathVariable String phone) {
        try {
            boolean exists = customerService.phoneNumberExists(businessId, phone);
            ApiResponse response = new ApiResponse("Phone validation completed", true);
            response.setData(Map.of("exists", exists));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Phone validation failed: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/validate-email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> validateEmailAddress(
            @PathVariable Long businessId,
            @PathVariable String email) {
        try {
            boolean exists = customerService.emailAddressExists(businessId, email);
            ApiResponse response = new ApiResponse("Email validation completed", true);
            response.setData(Map.of("exists", exists));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Email validation failed: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== CUSTOMER EXPORT ====================

    @GetMapping("/business/{businessId}/export")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> exportCustomers(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "csv") String format,
            @RequestParam(required = false) String filters) {
        try {
            byte[] exportData = customerService.exportCustomers(businessId, format, filters);
            ApiResponse response = new ApiResponse("Customers exported successfully", true);
            response.setData(Map.of(
                "format", format,
                "size", exportData.length,
                "data", exportData
            ));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Export failed: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
