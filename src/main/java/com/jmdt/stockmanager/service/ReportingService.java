package com.jmdt.stockmanager.service;

import com.jmdt.stockmanager.models.*;
import com.jmdt.stockmanager.enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service interface for comprehensive reporting and analytics
 * Handles business intelligence, dashboards, financial reports, and data insights
 */
public interface ReportingService {

    // ===== SALES REPORTS =====
    
    /**
     * Generate daily sales report
     */
    Map<String, Object> generateDailySalesReport(Long businessId, LocalDate date);
    
    /**
     * Generate sales report for date range
     */
    Map<String, Object> generateSalesReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate monthly sales summary
     */
    Map<String, Object> generateMonthlySalesReport(Long businessId, int year, int month);
    
    /**
     * Generate yearly sales summary
     */
    Map<String, Object> generateYearlySalesReport(Long businessId, int year);
    
    /**
     * Generate sales by product report
     */
    Map<String, Object> generateSalesByProductReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate sales by category report
     */
    Map<String, Object> generateSalesByCategoryReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate sales by customer report
     */
    Map<String, Object> generateSalesByCustomerReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate sales by payment method report
     */
    Map<String, Object> generateSalesByPaymentMethodReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate hourly sales pattern report
     */
    Map<String, Object> generateHourlySalesPatternReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    // ===== INVENTORY REPORTS =====
    
    /**
     * Generate current stock report
     */
    Map<String, Object> generateCurrentStockReport(Long businessId);
    
    /**
     * Generate low stock report
     */
    Map<String, Object> generateLowStockReport(Long businessId, Integer threshold);
    
    /**
     * Generate stock valuation report
     */
    Map<String, Object> generateStockValuationReport(Long businessId);
    
    /**
     * Generate inventory movement report
     */
    Map<String, Object> generateInventoryMovementReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate stock aging report
     */
    Map<String, Object> generateStockAgingReport(Long businessId);
    
    /**
     * Generate inventory turnover report
     */
    Map<String, Object> generateInventoryTurnoverReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate dead stock report
     */
    Map<String, Object> generateDeadStockReport(Long businessId, Integer daysThreshold);
    
    /**
     * Generate warehouse-wise stock report
     */
    Map<String, Object> generateWarehouseStockReport(Long businessId);
    
    /**
     * Generate batch expiry report
     */
    Map<String, Object> generateBatchExpiryReport(Long businessId, Integer daysThreshold);
    
    // ===== FINANCIAL REPORTS =====
    
    /**
     * Generate profit and loss report
     */
    Map<String, Object> generateProfitLossReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate revenue report
     */
    Map<String, Object> generateRevenueReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate payment collection report
     */
    Map<String, Object> generatePaymentCollectionReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate refund report
     */
    Map<String, Object> generateRefundReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate outstanding payments report
     */
    Map<String, Object> generateOutstandingPaymentsReport(Long businessId);
    
    /**
     * Generate tax report
     */
    Map<String, Object> generateTaxReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate cash flow report
     */
    Map<String, Object> generateCashFlowReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
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
     * Generate customer segmentation report
     */
    Map<String, Object> generateCustomerSegmentationReport(Long businessId);
    
    /**
     * Generate top customers report
     */
    Map<String, Object> generateTopCustomersReport(Long businessId, LocalDate startDate, LocalDate endDate, Integer limit);
    
    /**
     * Generate customer churn analysis report
     */
    Map<String, Object> generateCustomerChurnAnalysisReport(Long businessId);
    
    /**
     * Generate loyalty program performance report
     */
    Map<String, Object> generateLoyaltyProgramReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    // ===== RETURNS & WARRANTY REPORTS =====
    
    /**
     * Generate returns analysis report
     */
    Map<String, Object> generateReturnsAnalysisReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate warranty claims report
     */
    Map<String, Object> generateWarrantyClaimsReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate return reasons analysis
     */
    Map<String, Object> generateReturnReasonsAnalysis(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate product quality report (based on returns/warranties)
     */
    Map<String, Object> generateProductQualityReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    // ===== VENDOR & RMA REPORTS =====
    
    /**
     * Generate vendor performance report
     */
    Map<String, Object> generateVendorPerformanceReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate RMA analysis report
     */
    Map<String, Object> generateRMAAnalysisReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate vendor payment report
     */
    Map<String, Object> generateVendorPaymentReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    // ===== DASHBOARD DATA =====
    
    /**
     * Get main dashboard data
     */
    Map<String, Object> getDashboardData(Long businessId);
    
    /**
     * Get sales dashboard data
     */
    Map<String, Object> getSalesDashboardData(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get inventory dashboard data
     */
    Map<String, Object> getInventoryDashboardData(Long businessId);
    
    /**
     * Get customer dashboard data
     */
    Map<String, Object> getCustomerDashboardData(Long businessId);
    
    /**
     * Get financial dashboard data
     */
    Map<String, Object> getFinancialDashboardData(Long businessId, LocalDate startDate, LocalDate endDate);
    
    // ===== KEY PERFORMANCE INDICATORS (KPIs) =====
    
    /**
     * Get sales KPIs
     */
    Map<String, Object> getSalesKPIs(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get inventory KPIs
     */
    Map<String, Object> getInventoryKPIs(Long businessId);
    
    /**
     * Get customer KPIs
     */
    Map<String, Object> getCustomerKPIs(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get financial KPIs
     */
    Map<String, Object> getFinancialKPIs(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get operational KPIs
     */
    Map<String, Object> getOperationalKPIs(Long businessId, LocalDate startDate, LocalDate endDate);
    
    // ===== TREND ANALYSIS =====
    
    /**
     * Get sales trend analysis
     */
    Map<String, Object> getSalesTrendAnalysis(Long businessId, LocalDate startDate, LocalDate endDate, String period);
    
    /**
     * Get inventory trend analysis
     */
    Map<String, Object> getInventoryTrendAnalysis(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get customer trend analysis
     */
    Map<String, Object> getCustomerTrendAnalysis(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get product performance trend
     */
    Map<String, Object> getProductPerformanceTrend(Long businessId, LocalDate startDate, LocalDate endDate);
    
    // ===== COMPARATIVE ANALYSIS =====
    
    /**
     * Compare sales performance between periods
     */
    Map<String, Object> compareSalesPerformance(Long businessId, LocalDate period1Start, LocalDate period1End, 
                                               LocalDate period2Start, LocalDate period2End);
    
    /**
     * Compare inventory performance between periods
     */
    Map<String, Object> compareInventoryPerformance(Long businessId, LocalDate period1Start, LocalDate period1End, 
                                                   LocalDate period2Start, LocalDate period2End);
    
    /**
     * Compare customer metrics between periods
     */
    Map<String, Object> compareCustomerMetrics(Long businessId, LocalDate period1Start, LocalDate period1End, 
                                              LocalDate period2Start, LocalDate period2End);
    
    // ===== FORECASTING =====
    
    /**
     * Generate sales forecast
     */
    Map<String, Object> generateSalesForecast(Long businessId, Integer forecastDays);
    
    /**
     * Generate inventory demand forecast
     */
    Map<String, Object> generateInventoryDemandForecast(Long businessId, Integer forecastDays);
    
    /**
     * Generate customer behavior forecast
     */
    Map<String, Object> generateCustomerBehaviorForecast(Long businessId, Integer forecastDays);
    
    // ===== SNAPSHOTS MANAGEMENT =====
    
    /**
     * Create daily sales snapshot
     */
    DailySalesSnapshot createDailySalesSnapshot(Long businessId, LocalDate date);
    
    /**
     * Get sales snapshots for date range
     */
    List<DailySalesSnapshot> getSalesSnapshots(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Create inventory snapshots for all products
     */
    void createInventorySnapshots(Long businessId, LocalDate date);
    
    /**
     * Get inventory snapshots for date range
     */
    List<InventorySnapshot> getInventorySnapshots(Long businessId, LocalDate startDate, LocalDate endDate);
    
    // ===== AUDIT & COMPLIANCE REPORTS =====
    
    /**
     * Generate audit trail report
     */
    Map<String, Object> generateAuditTrailReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate user activity report
     */
    Map<String, Object> generateUserActivityReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate system usage report
     */
    Map<String, Object> generateSystemUsageReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate compliance report
     */
    Map<String, Object> generateComplianceReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    // ===== EXPORT FUNCTIONALITY =====
    
    /**
     * Export report to CSV
     */
    byte[] exportReportToCSV(String reportType, Long businessId, Map<String, Object> parameters);
    
    /**
     * Export report to Excel
     */
    byte[] exportReportToExcel(String reportType, Long businessId, Map<String, Object> parameters);
    
    /**
     * Export report to PDF
     */
    byte[] exportReportToPDF(String reportType, Long businessId, Map<String, Object> parameters);
    
    // ===== SCHEDULED REPORTS =====
    
    /**
     * Schedule automatic report generation
     */
    void scheduleReport(Long businessId, String reportType, String frequency, Map<String, Object> parameters);
    
    /**
     * Get scheduled reports for business
     */
    List<Map<String, Object>> getScheduledReports(Long businessId);
    
    /**
     * Cancel scheduled report
     */
    void cancelScheduledReport(Long scheduleId);
    
    // ===== CUSTOM REPORTS =====
    
    /**
     * Create custom report query
     */
    Map<String, Object> executeCustomQuery(Long businessId, String query, Map<String, Object> parameters);
    
    /**
     * Save custom report template
     */
    void saveCustomReportTemplate(Long businessId, String templateName, String query, Map<String, Object> metadata);
    
    /**
     * Get custom report templates
     */
    List<Map<String, Object>> getCustomReportTemplates(Long businessId);
    
    /**
     * Execute custom report template
     */
    Map<String, Object> executeCustomReportTemplate(Long businessId, String templateName, Map<String, Object> parameters);
    
    // ===== ALERT SYSTEM =====
    
    /**
     * Get business alerts
     */
    Map<String, List<Map<String, Object>>> getBusinessAlerts(Long businessId);
    
    /**
     * Create performance alert
     */
    void createPerformanceAlert(Long businessId, String alertType, Map<String, Object> thresholds);
    
    /**
     * Get alert history
     */
    Page<Map<String, Object>> getAlertHistory(Long businessId, Pageable pageable);
    
    // ===== ADDITIONAL CONTROLLER METHODS =====
    
    /**
     * Get sales summary report
     */
    Map<String, Object> getSalesSummaryReport(Long businessId, LocalDate startDate, LocalDate endDate, String groupBy);
    
    /**
     * Get sales trends report
     */
    Map<String, Object> getSalesTrendsReport(Long businessId, LocalDate startDate, LocalDate endDate, String interval);
    
    /**
     * Get top selling products report
     */
    Map<String, Object> getTopSellingProductsReport(Long businessId, LocalDate startDate, LocalDate endDate, int limit, String criteria);
    
    /**
     * Get sales performance report
     */
    Map<String, Object> getSalesPerformanceReport(Long businessId, LocalDate startDate, LocalDate endDate, Long userId);
    
    /**
     * Get inventory valuation report
     */
    Map<String, Object> getInventoryValuationReport(Long businessId, LocalDate asOfDate, String groupBy);
    
    /**
     * Get inventory movement report
     */
    Map<String, Object> getInventoryMovementReport(Long businessId, LocalDate startDate, LocalDate endDate, Long productId, Long warehouseId);
    
    /**
     * Get inventory turnover report
     */
    Map<String, Object> getInventoryTurnoverReport(Long businessId, LocalDate startDate, LocalDate endDate, int limit);
    
    /**
     * Get inventory aging report
     */
    Map<String, Object> getInventoryAgingReport(Long businessId, LocalDate asOfDate);
    
    /**
     * Get customer analysis report
     */
    Map<String, Object> getCustomerAnalysisReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get customer lifetime value report
     */
    Map<String, Object> getCustomerLifetimeValueReport(Long businessId, int limit);
    
    /**
     * Get customer retention report
     */
    Map<String, Object> getCustomerRetentionReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get customer segmentation report
     */
    Map<String, Object> getCustomerSegmentationReport(Long businessId);
    
    /**
     * Get profit and loss report
     */
    Map<String, Object> getProfitLossReport(Long businessId, LocalDate startDate, LocalDate endDate, String groupBy);
    
    /**
     * Get cash flow report
     */
    Map<String, Object> getCashFlowReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get revenue analysis report
     */
    Map<String, Object> getRevenueAnalysisReport(Long businessId, LocalDate startDate, LocalDate endDate, String interval);
    
    /**
     * Get returns analysis report
     */
    Map<String, Object> getReturnsAnalysisReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get warranty claims report
     */
    Map<String, Object> getWarrantyClaimsReport(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get staff performance report
     */
    Map<String, Object> getStaffPerformanceReport(Long businessId, LocalDate startDate, LocalDate endDate, Long userId);
    
    /**
     * Get dashboard overview
     */
    Map<String, Object> getDashboardOverview(Long businessId);
    
    /**
     * Get key performance indicators
     */
    Map<String, Object> getKeyPerformanceIndicators(Long businessId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get dashboard charts
     */
    Map<String, Object> getDashboardCharts(Long businessId, String chartType, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get period over period report
     */
    Map<String, Object> getPeriodOverPeriodReport(Long businessId, LocalDate currentStartDate, LocalDate currentEndDate, 
                                                 LocalDate previousStartDate, LocalDate previousEndDate, String metric);
    
    /**
     * Get year over year report
     */
    Map<String, Object> getYearOverYearReport(Long businessId, int currentYear, int previousYear, String metric);
    
    /**
     * Export comprehensive report
     */
    byte[] exportComprehensiveReport(Long businessId, LocalDate startDate, LocalDate endDate, String format, List<String> sections);
    
    /**
     * Export custom report
     */
    byte[] exportCustomReport(Long businessId, String reportType, LocalDate startDate, LocalDate endDate, String format, Map<String, Object> filters);
    
    /**
     * Create scheduled report
     */
    Map<String, Object> createScheduledReport(Long businessId, String reportType, String schedule, String format, 
                                            List<String> recipients, Map<String, Object> parameters);
    
    /**
     * Delete scheduled report
     */
    void deleteScheduledReport(Long reportId);
}
