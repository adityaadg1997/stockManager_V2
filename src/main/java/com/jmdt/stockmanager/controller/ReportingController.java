package com.jmdt.stockmanager.controller;

import com.jmdt.stockmanager.exception.ResourceNotFoundException;
import com.jmdt.stockmanager.payloads.ApiResponse;
import com.jmdt.stockmanager.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportingController {

    @Autowired
    private ReportingService reportingService;

    // ==================== SALES REPORTS ====================

    @GetMapping("/business/{businessId}/sales/summary")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getSalesSummaryReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) String groupBy) {
        try {
            Map<String, Object> report = reportingService.getSalesSummaryReport(businessId, startDate, endDate, groupBy);
            ApiResponse response = new ApiResponse("Sales summary report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate sales summary report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/sales/trends")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getSalesTrendsReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "daily") String interval) {
        try {
            Map<String, Object> report = reportingService.getSalesTrendsReport(businessId, startDate, endDate, interval);
            ApiResponse response = new ApiResponse("Sales trends report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate sales trends report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/sales/top-products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getTopSellingProductsReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "quantity") String criteria) {
        try {
            Map<String, Object> report = reportingService.getTopSellingProductsReport(
                businessId, startDate, endDate, limit, criteria);
            ApiResponse response = new ApiResponse("Top selling products report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate top products report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/sales/performance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getSalesPerformanceReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) Long userId) {
        try {
            Map<String, Object> report = reportingService.getSalesPerformanceReport(businessId, startDate, endDate, userId);
            ApiResponse response = new ApiResponse("Sales performance report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate sales performance report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== INVENTORY REPORTS ====================

    @GetMapping("/business/{businessId}/inventory/valuation")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getInventoryValuationReport(
            @PathVariable Long businessId,
            @RequestParam(required = false) LocalDate asOfDate,
            @RequestParam(required = false) String groupBy) {
        try {
            Map<String, Object> report = reportingService.getInventoryValuationReport(businessId, asOfDate, groupBy);
            ApiResponse response = new ApiResponse("Inventory valuation report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate inventory valuation report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/inventory/movement")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getInventoryMovementReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId) {
        try {
            Map<String, Object> report = reportingService.getInventoryMovementReport(
                businessId, startDate, endDate, productId, warehouseId);
            ApiResponse response = new ApiResponse("Inventory movement report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate inventory movement report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/inventory/turnover")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getInventoryTurnoverReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            Map<String, Object> report = reportingService.getInventoryTurnoverReport(businessId, startDate, endDate, limit);
            ApiResponse response = new ApiResponse("Inventory turnover report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate inventory turnover report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/inventory/aging")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getInventoryAgingReport(
            @PathVariable Long businessId,
            @RequestParam(required = false) LocalDate asOfDate) {
        try {
            Map<String, Object> report = reportingService.getInventoryAgingReport(businessId, asOfDate);
            ApiResponse response = new ApiResponse("Inventory aging report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate inventory aging report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== CUSTOMER REPORTS ====================

    @GetMapping("/business/{businessId}/customers/analysis")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getCustomerAnalysisReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        try {
            Map<String, Object> report = reportingService.getCustomerAnalysisReport(businessId, startDate, endDate);
            ApiResponse response = new ApiResponse("Customer analysis report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate customer analysis report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/customers/lifetime-value")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getCustomerLifetimeValueReport(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            Map<String, Object> report = reportingService.getCustomerLifetimeValueReport(businessId, limit);
            ApiResponse response = new ApiResponse("Customer lifetime value report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate customer lifetime value report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/customers/retention")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getCustomerRetentionReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        try {
            Map<String, Object> report = reportingService.getCustomerRetentionReport(businessId, startDate, endDate);
            ApiResponse response = new ApiResponse("Customer retention report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate customer retention report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/customers/segmentation")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getCustomerSegmentationReport(@PathVariable Long businessId) {
        try {
            Map<String, Object> report = reportingService.getCustomerSegmentationReport(businessId);
            ApiResponse response = new ApiResponse("Customer segmentation report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate customer segmentation report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== FINANCIAL REPORTS ====================

    @GetMapping("/business/{businessId}/financial/profit-loss")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getProfitLossReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "monthly") String groupBy) {
        try {
            Map<String, Object> report = reportingService.getProfitLossReport(businessId, startDate, endDate, groupBy);
            ApiResponse response = new ApiResponse("Profit & Loss report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate P&L report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/financial/cash-flow")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getCashFlowReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        try {
            Map<String, Object> report = reportingService.getCashFlowReport(businessId, startDate, endDate);
            ApiResponse response = new ApiResponse("Cash flow report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate cash flow report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/financial/revenue-analysis")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getRevenueAnalysisReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "monthly") String interval) {
        try {
            Map<String, Object> report = reportingService.getRevenueAnalysisReport(businessId, startDate, endDate, interval);
            ApiResponse response = new ApiResponse("Revenue analysis report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate revenue analysis report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== OPERATIONAL REPORTS ====================

    @GetMapping("/business/{businessId}/operations/returns-analysis")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getReturnsAnalysisReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        try {
            Map<String, Object> report = reportingService.getReturnsAnalysisReport(businessId, startDate, endDate);
            ApiResponse response = new ApiResponse("Returns analysis report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate returns analysis report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/operations/warranty-claims")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getWarrantyClaimsReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        try {
            Map<String, Object> report = reportingService.getWarrantyClaimsReport(businessId, startDate, endDate);
            ApiResponse response = new ApiResponse("Warranty claims report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate warranty claims report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/operations/staff-performance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getStaffPerformanceReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) Long userId) {
        try {
            Map<String, Object> report = reportingService.getStaffPerformanceReport(businessId, startDate, endDate, userId);
            ApiResponse response = new ApiResponse("Staff performance report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate staff performance report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== DASHBOARD ANALYTICS ====================

    @GetMapping("/business/{businessId}/dashboard/overview")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getDashboardOverview(@PathVariable Long businessId) {
        try {
            Map<String, Object> dashboard = reportingService.getDashboardOverview(businessId);
            ApiResponse response = new ApiResponse("Dashboard overview retrieved successfully", true);
            response.setData(dashboard);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve dashboard overview: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/dashboard/kpis")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getKeyPerformanceIndicators(
            @PathVariable Long businessId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        try {
            Map<String, Object> kpis = reportingService.getKeyPerformanceIndicators(businessId, startDate, endDate);
            ApiResponse response = new ApiResponse("KPIs retrieved successfully", true);
            response.setData(kpis);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve KPIs: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/dashboard/alerts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse> getBusinessAlerts(@PathVariable Long businessId) {
        try {
            Map<String, List<Map<String, Object>>> alerts = reportingService.getBusinessAlerts(businessId);
            ApiResponse response = new ApiResponse("Business alerts retrieved successfully", true);
            response.setData(alerts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve business alerts: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/dashboard/charts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getDashboardCharts(
            @PathVariable Long businessId,
            @RequestParam(required = false) String chartType,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        try {
            Map<String, Object> charts = reportingService.getDashboardCharts(businessId, chartType, startDate, endDate);
            ApiResponse response = new ApiResponse("Dashboard charts retrieved successfully", true);
            response.setData(charts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve dashboard charts: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== COMPARATIVE REPORTS ====================

    @GetMapping("/business/{businessId}/comparative/period-over-period")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getPeriodOverPeriodReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate currentStartDate,
            @RequestParam LocalDate currentEndDate,
            @RequestParam LocalDate previousStartDate,
            @RequestParam LocalDate previousEndDate,
            @RequestParam(defaultValue = "sales") String metric) {
        try {
            Map<String, Object> report = reportingService.getPeriodOverPeriodReport(
                businessId, currentStartDate, currentEndDate, previousStartDate, previousEndDate, metric);
            ApiResponse response = new ApiResponse("Period over period report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate period comparison report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/comparative/year-over-year")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getYearOverYearReport(
            @PathVariable Long businessId,
            @RequestParam int currentYear,
            @RequestParam int previousYear,
            @RequestParam(defaultValue = "sales") String metric) {
        try {
            Map<String, Object> report = reportingService.getYearOverYearReport(businessId, currentYear, previousYear, metric);
            ApiResponse response = new ApiResponse("Year over year report generated successfully", true);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to generate year over year report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== EXPORT REPORTS ====================

    @GetMapping("/business/{businessId}/export/comprehensive")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> exportComprehensiveReport(
            @PathVariable Long businessId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "pdf") String format,
            @RequestParam(required = false) List<String> sections) {
        try {
            byte[] reportData = reportingService.exportComprehensiveReport(businessId, startDate, endDate, format, sections);
            ApiResponse response = new ApiResponse("Comprehensive report exported successfully", true);
            response.setData(Map.of(
                "format", format,
                "size", reportData.length,
                "data", reportData
            ));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to export comprehensive report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/export/custom")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> exportCustomReport(
            @PathVariable Long businessId,
            @RequestParam String reportType,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "excel") String format,
            @RequestParam(required = false) Map<String, Object> filters) {
        try {
            byte[] reportData = reportingService.exportCustomReport(businessId, reportType, startDate, endDate, format, filters);
            ApiResponse response = new ApiResponse("Custom report exported successfully", true);
            response.setData(Map.of(
                "report_type", reportType,
                "format", format,
                "size", reportData.length,
                "data", reportData
            ));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to export custom report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ==================== SCHEDULED REPORTS ====================

    @PostMapping("/business/{businessId}/scheduled")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> createScheduledReport(
            @PathVariable Long businessId,
            @RequestParam String reportType,
            @RequestParam String schedule, // cron expression
            @RequestParam String format,
            @RequestParam List<String> recipients,
            @RequestParam(required = false) Map<String, Object> parameters) {
        try {
            Map<String, Object> scheduledReport = reportingService.createScheduledReport(
                businessId, reportType, schedule, format, recipients, parameters);
            ApiResponse response = new ApiResponse("Scheduled report created successfully", true);
            response.setData(scheduledReport);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to create scheduled report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/business/{businessId}/scheduled")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getScheduledReports(@PathVariable Long businessId) {
        try {
            List<Map<String, Object>> scheduledReports = reportingService.getScheduledReports(businessId);
            ApiResponse response = new ApiResponse("Scheduled reports retrieved successfully", true);
            response.setData(scheduledReports);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to retrieve scheduled reports: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/scheduled/{reportId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> deleteScheduledReport(@PathVariable Long reportId) {
        try {
            reportingService.deleteScheduledReport(reportId);
            ApiResponse response = new ApiResponse("Scheduled report deleted successfully", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to delete scheduled report: " + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
