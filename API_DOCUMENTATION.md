# Stock Manager API Documentation

Complete API documentation with cURL examples for testing all endpoints.

## Base URL
```
http://localhost:8080
```

## Authentication
Most endpoints require JWT authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## Response Format
All APIs return responses in the following format:
```json
{
  "message": "Success message",
  "success": true,
  "data": { ... }
}
```

---
# 0. Onboarding APIs
## 0.1 Registers a new business, admin user, and starter subscription in a single request.
```bash
curl -X POST http://localhost:8080/api/onboarding/register \
  -H "Content-Type: application/json" \
  -d '{
    "businessName": "ABC Shop",
    "businessEmail": "owner@abc.com",
    "adminName": "John Doe",
    "adminEmail": "owner@abc.com",
    "adminPassword": "yourPassword"
  }'
```  


# 1. BUSINESS MANAGEMENT APIs

## 1.1 Create Business
```bash
curl -X POST http://localhost:8080/api/businesses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Tech Solutions Inc",
    "contactEmail": "admin@techsolutions.com",
    "phone": "+1-555-0123",
    "address": "123 Business St, City, State 12345",
    "logoUrl": "https://example.com/logo.png",
    "plan": "MONTHLY",
    "timezone": "America/New_York"
  }'
```

## 1.2 Get Business by ID
```bash
curl -X GET http://localhost:8080/api/businesses/1 \
  -H "Authorization: Bearer <token>"
```

## 1.3 Update Business
```bash
curl -X PUT http://localhost:8080/api/businesses/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Updated Tech Solutions Inc",
    "contactEmail": "admin@techsolutions.com",
    "phone": "+1-555-0124",
    "address": "456 New Business Ave, City, State 12345",
    "plan": "YEARLY",
    "timezone": "America/New_York"
  }'
```

## 1.4 Get All Businesses
```bash
curl -X GET http://localhost:8080/api/businesses \
  -H "Authorization: Bearer <token>"
```

## 1.5 Get Businesses by Plan
```bash
curl -X GET "http://localhost:8080/api/businesses/plan/MONTHLY" \
  -H "Authorization: Bearer <token>"
```

## 1.6 Delete Business
```bash
curl -X DELETE http://localhost:8080/api/businesses/1 \
  -H "Authorization: Bearer <token>"
```

## 1.7 Check Business Name Availability
```bash
curl -X GET "http://localhost:8080/api/businesses/check-name?name=Tech%20Solutions" \
  -H "Authorization: Bearer <token>"
```

## 1.8 Check Email Availability
```bash
curl -X GET "http://localhost:8080/api/businesses/check-email?email=admin@techsolutions.com" \
  -H "Authorization: Bearer <token>"
```

---

# 2. PRODUCT MANAGEMENT APIs

## 2.1 Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Wireless Headphones",
    "skuCode": "WH-001",
    "category": "Electronics",
    "quantity": 100,
    "costPrice": 50.00,
    "sellingPrice": 99.99,
    "imageUrl": "https://example.com/headphones.jpg",
    "isActive": true,
    "hasWarranty": true,
    "warrantyDurationDays": 365,
    "batchTracked": false,
    "businessId": 1,
    "vendorId": 1
  }'
```

## 2.2 Get Product by ID
```bash
curl -X GET http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <token>"
```

## 2.3 Update Product
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Premium Wireless Headphones",
    "skuCode": "WH-001",
    "category": "Electronics",
    "quantity": 150,
    "costPrice": 55.00,
    "sellingPrice": 109.99,
    "isActive": true,
    "hasWarranty": true,
    "warrantyDurationDays": 730
  }'
```

## 2.4 Get Products by Business
```bash
curl -X GET http://localhost:8080/api/products/business/1 \
  -H "Authorization: Bearer <token>"
```

## 2.5 Get Active Products
```bash
curl -X GET http://localhost:8080/api/products/business/1/active \
  -H "Authorization: Bearer <token>"
```

## 2.6 Search Products by Name
```bash
curl -X GET "http://localhost:8080/api/products/business/1/search?name=headphones" \
  -H "Authorization: Bearer <token>"
```

## 2.7 Get Products by Category
```bash
curl -X GET http://localhost:8080/api/products/business/1/category/Electronics \
  -H "Authorization: Bearer <token>"
```

## 2.8 Get Products by Vendor
```bash
curl -X GET http://localhost:8080/api/products/business/1/vendor/1 \
  -H "Authorization: Bearer <token>"
```

## 2.9 Get Low Stock Products
```bash
curl -X GET "http://localhost:8080/api/products/business/1/low-stock?threshold=10" \
  -H "Authorization: Bearer <token>"
```

## 2.10 Create Product Variant
```bash
curl -X POST http://localhost:8080/api/products/1/variants \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Black",
    "skuSuffix": "-BLK",
    "quantity": 50,
    "priceAdjustment": 0.00
  }'
```

## 2.11 Get Product Variants
```bash
curl -X GET http://localhost:8080/api/products/1/variants \
  -H "Authorization: Bearer <token>"
```

## 2.12 Update Product Variant
```bash
curl -X PUT http://localhost:8080/api/products/variants/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Matte Black",
    "skuSuffix": "-MBLK",
    "quantity": 75,
    "priceAdjustment": 5.00
  }'
```

## 2.13 Create Product Batch
```bash
curl -X POST http://localhost:8080/api/products/1/batches \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "batchNumber": "BATCH-001",
    "manufacturingDate": "2024-01-15",
    "expiryDate": "2026-01-15",
    "quantity": 100,
    "warehouseId": 1
  }'
```

## 2.14 Get Product Batches
```bash
curl -X GET http://localhost:8080/api/products/1/batches \
  -H "Authorization: Bearer <token>"
```

## 2.15 Get Expiring Batches
```bash
curl -X GET "http://localhost:8080/api/products/business/1/batches/expiring?days=30" \
  -H "Authorization: Bearer <token>"
```

## 2.16 Update Product Stock
```bash
curl -X PUT "http://localhost:8080/api/products/1/stock?quantity=200" \
  -H "Authorization: Bearer <token>"
```

## 2.17 Adjust Product Stock
```bash
curl -X POST "http://localhost:8080/api/products/1/stock/adjust?adjustment=50&reason=Restock" \
  -H "Authorization: Bearer <token>"
```

## 2.18 Check SKU Uniqueness
```bash
curl -X GET "http://localhost:8080/api/products/business/1/sku-unique?skuCode=WH-002" \
  -H "Authorization: Bearer <token>"
```

## 2.19 Activate Product
```bash
curl -X POST http://localhost:8080/api/products/1/activate \
  -H "Authorization: Bearer <token>"
```

## 2.20 Deactivate Product
```bash
curl -X POST http://localhost:8080/api/products/1/deactivate \
  -H "Authorization: Bearer <token>"
```

## 2.21 Delete Product
```bash
curl -X DELETE http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <token>"
```

---

# 3. SALES MANAGEMENT APIs

## 3.1 Create Sale
```bash
curl -X POST http://localhost:8080/api/sales \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "customerId": 1,
    "businessId": 1,
    "totalAmount": 199.98,
    "discountAmount": 10.00,
    "taxAmount": 15.99,
    "invoiceNo": "INV-001",
    "saleItems": [
      {
        "productId": 1,
        "quantity": 2,
        "priceEach": 99.99,
        "discount": 5.00,
        "taxApplied": 7.99
      }
    ]
  }'
```

## 3.2 Get Sale by ID
```bash
curl -X GET http://localhost:8080/api/sales/1 \
  -H "Authorization: Bearer <token>"
```

## 3.3 Update Sale
```bash
curl -X PUT http://localhost:8080/api/sales/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "totalAmount": 189.98,
    "discountAmount": 20.00,
    "taxAmount": 15.99,
    "status": "COMPLETED"
  }'
```

## 3.4 Get Sales by Business
```bash
curl -X GET "http://localhost:8080/api/sales/business/1?page=0&size=10" \
  -H "Authorization: Bearer <token>"
```

## 3.5 Get Sales by Customer
```bash
curl -X GET "http://localhost:8080/api/sales/customer/1?page=0&size=10" \
  -H "Authorization: Bearer <token>"
```

## 3.6 Get Sales by Date Range
```bash
curl -X GET "http://localhost:8080/api/sales/business/1/date-range?startDate=2024-01-01&endDate=2024-12-31" \
  -H "Authorization: Bearer <token>"
```

## 3.7 Get Sales by Status
```bash
curl -X GET "http://localhost:8080/api/sales/business/1/status/COMPLETED?page=0&size=10" \
  -H "Authorization: Bearer <token>"
```

## 3.8 Get Today's Sales
```bash
curl -X GET http://localhost:8080/api/sales/business/1/today \
  -H "Authorization: Bearer <token>"
```

## 3.9 Search Sales by Invoice
```bash
curl -X GET "http://localhost:8080/api/sales/business/1/search?invoiceNo=INV-001" \
  -H "Authorization: Bearer <token>"
```

## 3.10 Add Payment to Sale
```bash
curl -X POST http://localhost:8080/api/sales/1/payments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "amountPaid": 199.98,
    "paymentMethod": "CARD",
    "referenceId": "TXN-12345",
    "notes": "Payment via credit card"
  }'
```

## 3.11 Get Sale Payments
```bash
curl -X GET http://localhost:8080/api/sales/1/payments \
  -H "Authorization: Bearer <token>"
```

## 3.12 Process Refund
```bash
curl -X POST http://localhost:8080/api/sales/1/refund \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "refundAmount": 99.99,
    "refundMethod": "CARD",
    "reason": "Product defective",
    "refundItems": [
      {
        "saleItemId": 1,
        "quantity": 1
      }
    ]
  }'
```

## 3.13 Cancel Sale
```bash
curl -X POST http://localhost:8080/api/sales/1/cancel \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "reason": "Customer requested cancellation"
  }'
```

## 3.14 Get Sale Analytics
```bash
curl -X GET "http://localhost:8080/api/sales/business/1/analytics?startDate=2024-01-01&endDate=2024-12-31" \
  -H "Authorization: Bearer <token>"
```

## 3.15 Get Sales Summary
```bash
curl -X GET "http://localhost:8080/api/sales/business/1/summary?period=MONTHLY" \
  -H "Authorization: Bearer <token>"
```

---

# 4. CUSTOMER MANAGEMENT APIs

## 4.1 Create Customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "John Doe",
    "phone": "+1-555-0199",
    "email": "john.doe@email.com",
    "gstin": "GST123456789",
    "address": "789 Customer Lane, City, State 12345",
    "businessId": 1
  }'
```

## 4.2 Get Customer by ID
```bash
curl -X GET http://localhost:8080/api/customers/1 \
  -H "Authorization: Bearer <token>"
```

## 4.3 Update Customer
```bash
curl -X PUT http://localhost:8080/api/customers/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "John Smith",
    "phone": "+1-555-0200",
    "email": "john.smith@email.com",
    "address": "890 Updated Lane, City, State 12345"
  }'
```

## 4.4 Get Customers by Business
```bash
curl -X GET "http://localhost:8080/api/customers/business/1?page=0&size=10" \
  -H "Authorization: Bearer <token>"
```

## 4.5 Search Customers
```bash
curl -X GET "http://localhost:8080/api/customers/business/1/search?query=john" \
  -H "Authorization: Bearer <token>"
```

## 4.6 Get Customer by Phone
```bash
curl -X GET "http://localhost:8080/api/customers/business/1/phone/+1-555-0199" \
  -H "Authorization: Bearer <token>"
```

## 4.7 Get Customer by Email
```bash
curl -X GET "http://localhost:8080/api/customers/business/1/email/john.doe@email.com" \
  -H "Authorization: Bearer <token>"
```

## 4.8 Get Top Customers
```bash
curl -X GET "http://localhost:8080/api/customers/business/1/top?limit=10" \
  -H "Authorization: Bearer <token>"
```

## 4.9 Get Customer Purchase History
```bash
curl -X GET "http://localhost:8080/api/customers/1/purchase-history?page=0&size=10" \
  -H "Authorization: Bearer <token>"
```

## 4.10 Update Customer Loyalty Points
```bash
curl -X PUT "http://localhost:8080/api/customers/1/loyalty-points?points=100" \
  -H "Authorization: Bearer <token>"
```

## 4.11 Get Customer Analytics
```bash
curl -X GET "http://localhost:8080/api/customers/business/1/analytics?startDate=2024-01-01&endDate=2024-12-31" \
  -H "Authorization: Bearer <token>"
```

## 4.12 Delete Customer
```bash
curl -X DELETE http://localhost:8080/api/customers/1 \
  -H "Authorization: Bearer <token>"
```

---

# 5. INVENTORY MANAGEMENT APIs

## 5.1 Create Warehouse
```bash
curl -X POST http://localhost:8080/api/inventory/warehouses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Main Warehouse",
    "address": "123 Warehouse St, Industrial Area",
    "businessId": 1,
    "isActive": true
  }'
```

## 5.2 Get Warehouse by ID
```bash
curl -X GET http://localhost:8080/api/inventory/warehouses/1 \
  -H "Authorization: Bearer <token>"
```

## 5.3 Get Warehouses by Business
```bash
curl -X GET http://localhost:8080/api/inventory/warehouses/business/1 \
  -H "Authorization: Bearer <token>"
```

## 5.4 Update Warehouse
```bash
curl -X PUT http://localhost:8080/api/inventory/warehouses/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Updated Main Warehouse",
    "address": "456 New Warehouse Ave, Industrial Area",
    "isActive": true
  }'
```

## 5.5 Get Stock by Warehouse
```bash
curl -X GET http://localhost:8080/api/inventory/warehouses/1/stock \
  -H "Authorization: Bearer <token>"
```

## 5.6 Transfer Stock Between Warehouses
```bash
curl -X POST http://localhost:8080/api/inventory/stock/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "productId": 1,
    "fromWarehouseId": 1,
    "toWarehouseId": 2,
    "quantity": 50,
    "reason": "Stock rebalancing"
  }'
```

## 5.7 Adjust Stock
```bash
curl -X POST http://localhost:8080/api/inventory/stock/adjust \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "productId": 1,
    "warehouseId": 1,
    "adjustmentType": "ADD",
    "quantity": 100,
    "reason": "New stock arrival"
  }'
```

## 5.8 Get Stock Movements
```bash
curl -X GET "http://localhost:8080/api/inventory/stock/movements?businessId=1&startDate=2024-01-01&endDate=2024-12-31" \
  -H "Authorization: Bearer <token>"
```

## 5.9 Get Low Stock Alert
```bash
curl -X GET "http://localhost:8080/api/inventory/business/1/low-stock?threshold=10" \
  -H "Authorization: Bearer <token>"
```

## 5.10 Get Stock Valuation
```bash
curl -X GET http://localhost:8080/api/inventory/business/1/valuation \
  -H "Authorization: Bearer <token>"
```

## 5.11 Perform Stock Count
```bash
curl -X POST http://localhost:8080/api/inventory/stock/count \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "warehouseId": 1,
    "stockCounts": [
      {
        "productId": 1,
        "countedQuantity": 95,
        "notes": "Physical count verification"
      }
    ]
  }'
```

## 5.12 Get Inventory Report
```bash
curl -X GET "http://localhost:8080/api/inventory/business/1/report?reportType=SUMMARY" \
  -H "Authorization: Bearer <token>"
```

---

# 6. REPORTING & ANALYTICS APIs

## 6.1 Sales Reports

### Get Sales Summary Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/sales/summary?startDate=2024-01-01&endDate=2024-12-31&groupBy=monthly" \
  -H "Authorization: Bearer <token>"
```

### Get Sales Trends Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/sales/trends?startDate=2024-01-01&endDate=2024-12-31&interval=daily" \
  -H "Authorization: Bearer <token>"
```

### Get Top Selling Products Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/sales/top-products?startDate=2024-01-01&endDate=2024-12-31&limit=10&criteria=quantity" \
  -H "Authorization: Bearer <token>"
```

### Get Sales Performance Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/sales/performance?startDate=2024-01-01&endDate=2024-12-31&userId=1" \
  -H "Authorization: Bearer <token>"
```

## 6.2 Inventory Reports

### Get Inventory Valuation Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/inventory/valuation?asOfDate=2024-12-31&groupBy=category" \
  -H "Authorization: Bearer <token>"
```

### Get Inventory Movement Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/inventory/movement?startDate=2024-01-01&endDate=2024-12-31&productId=1&warehouseId=1" \
  -H "Authorization: Bearer <token>"
```

### Get Inventory Turnover Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/inventory/turnover?startDate=2024-01-01&endDate=2024-12-31&limit=10" \
  -H "Authorization: Bearer <token>"
```

### Get Inventory Aging Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/inventory/aging?asOfDate=2024-12-31" \
  -H "Authorization: Bearer <token>"
```

## 6.3 Customer Reports

### Get Customer Analysis Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/customers/analysis?startDate=2024-01-01&endDate=2024-12-31" \
  -H "Authorization: Bearer <token>"
```

### Get Customer Lifetime Value Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/customers/lifetime-value?limit=10" \
  -H "Authorization: Bearer <token>"
```

### Get Customer Retention Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/customers/retention?startDate=2024-01-01&endDate=2024-12-31" \
  -H "Authorization: Bearer <token>"
```

### Get Customer Segmentation Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/customers/segmentation" \
  -H "Authorization: Bearer <token>"
```

## 6.4 Financial Reports

### Get Profit & Loss Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/financial/profit-loss?startDate=2024-01-01&endDate=2024-12-31&groupBy=monthly" \
  -H "Authorization: Bearer <token>"
```

### Get Cash Flow Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/financial/cash-flow?startDate=2024-01-01&endDate=2024-12-31" \
  -H "Authorization: Bearer <token>"
```

### Get Revenue Analysis Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/financial/revenue-analysis?startDate=2024-01-01&endDate=2024-12-31&interval=monthly" \
  -H "Authorization: Bearer <token>"
```

## 6.5 Operational Reports

### Get Returns Analysis Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/operations/returns-analysis?startDate=2024-01-01&endDate=2024-12-31" \
  -H "Authorization: Bearer <token>"
```

### Get Warranty Claims Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/operations/warranty-claims?startDate=2024-01-01&endDate=2024-12-31" \
  -H "Authorization: Bearer <token>"
```

### Get Staff Performance Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/operations/staff-performance?startDate=2024-01-01&endDate=2024-12-31&userId=1" \
  -H "Authorization: Bearer <token>"
```

## 6.6 Dashboard Analytics

### Get Dashboard Overview
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/dashboard/overview" \
  -H "Authorization: Bearer <token>"
```

### Get Key Performance Indicators
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/dashboard/kpis?startDate=2024-01-01&endDate=2024-12-31" \
  -H "Authorization: Bearer <token>"
```

### Get Business Alerts
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/dashboard/alerts" \
  -H "Authorization: Bearer <token>"
```

### Get Dashboard Charts
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/dashboard/charts?chartType=sales&startDate=2024-01-01&endDate=2024-12-31" \
  -H "Authorization: Bearer <token>"
```

## 6.7 Comparative Reports

### Get Period Over Period Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/comparative/period-over-period?currentStartDate=2024-07-01&currentEndDate=2024-12-31&previousStartDate=2024-01-01&previousEndDate=2024-06-30&metric=sales" \
  -H "Authorization: Bearer <token>"
```

### Get Year Over Year Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/comparative/year-over-year?currentYear=2024&previousYear=2023&metric=sales" \
  -H "Authorization: Bearer <token>"
```

## 6.8 Export Reports

### Export Comprehensive Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/export/comprehensive?startDate=2024-01-01&endDate=2024-12-31&format=pdf&sections=sales,inventory,customers" \
  -H "Authorization: Bearer <token>"
```

### Export Custom Report
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/export/custom?reportType=sales&startDate=2024-01-01&endDate=2024-12-31&format=excel" \
  -H "Authorization: Bearer <token>"
```

## 6.9 Scheduled Reports

### Create Scheduled Report
```bash
curl -X POST "http://localhost:8080/api/reports/business/1/scheduled" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "reportType": "sales_summary",
    "schedule": "0 0 9 * * MON",
    "format": "pdf",
    "recipients": ["admin@company.com", "manager@company.com"],
    "parameters": {
      "groupBy": "weekly",
      "includeCharts": true
    }
  }'
```

### Get Scheduled Reports
```bash
curl -X GET "http://localhost:8080/api/reports/business/1/scheduled" \
  -H "Authorization: Bearer <token>"
```

### Delete Scheduled Report
```bash
curl -X DELETE "http://localhost:8080/api/reports/scheduled/1" \
  -H "Authorization: Bearer <token>"
```

---

# 7. AUTHENTICATION APIs

## 7.1 User Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@techsolutions.com",
    "password": "password123"
  }'
```

## 7.2 User Registration
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Admin User",
    "email": "admin@techsolutions.com",
    "password": "password123",
    "businessId": 1,
    "role": "ADMIN"
  }'
```

## 7.3 Refresh Token
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <refresh-token>"
```

## 7.4 Logout
```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer <token>"
```

---

# 8. USER MANAGEMENT APIs

## 8.1 Get Current User Profile
```bash
curl -X GET http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer <token>"
```

## 8.2 Update User Profile
```bash
curl -X PUT http://localhost:8080/api/users/profile \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Updated Admin User",
    "email": "updated.admin@techsolutions.com"
  }'
```

## 8.3 Change Password
```bash
curl -X PUT http://localhost:8080/api/users/change-password \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "currentPassword": "password123",
    "newPassword": "newpassword456"
  }'
```

## 8.4 Get Users by Business
```bash
curl -X GET "http://localhost:8080/api/users/business/1?page=0&size=10" \
  -H "Authorization: Bearer <token>"
```

## 8.5 Create User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Staff User",
    "email": "staff@techsolutions.com",
    "password": "password123",
    "businessId": 1,
    "role": "STAFF"
  }'
```

## 8.6 Update User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Updated Staff User",
    "email": "updated.staff@techsolutions.com",
    "role": "MANAGER"
  }'
```

## 8.7 Delete User
```bash
curl -X DELETE http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer <token>"
```

---

# 9. TESTING WORKFLOW

## 9.1 Basic Testing Sequence

### Step 1: Start the Application
```bash
mvn spring-boot:run
```

### Step 2: Create a Business (First)
```bash
curl -X POST http://localhost:8080/api/businesses \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Business",
    "contactEmail": "test@business.com",
    "phone": "+1-555-0123",
    "plan": "MONTHLY"
  }'
```

### Step 3: Register/Login User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Admin",
    "email": "admin@business.com",
    "password": "password123",
    "businessId": 1,
    "role": "ADMIN"
  }'
```

### Step 4: Login to Get JWT Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@business.com",
    "password": "password123"
  }'
```

### Step 5: Use the JWT Token for Subsequent Requests
Replace `<token>` in all other API calls with the JWT token received from login.

## 9.2 Sample Test Data

### Create Sample Vendor
```bash
curl -X POST http://localhost:8080/api/vendors \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Tech Supplier Inc",
    "contactEmail": "supplier@techsupplier.com",
    "phone": "+1-555-0200",
    "address": "456 Supplier Ave, City, State",
    "paymentTerms": "Net 30",
    "businessId": 1
  }'
```

### Get Vendor by ID
```bash
curl -X GET http://localhost:8080/api/vendors/1 \
  -H "Authorization: Bearer <token>"
````

### Update Vendor
```bash
curl -X PUT http://localhost:8080/api/vendors/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Updated Supplier Inc",
    "contactEmail": "updated@supplier.com",
    "phone": "+1-555-0201",
    "address": "789 New Supplier Ave, City, State",
    "paymentTerms": "Net 45"
  }'
```

### Get Vendors by Business
```bash
curl -X GET http://localhost:8080/api/vendors/business/1 \
  -H "Authorization: Bearer <token>"
```

### Delete Vendor
```bash
curl -X DELETE http://localhost:8080/api/vendors/1 \
  -H "Authorization: Bearer <token>"
```


### Create Sample Warehouse
```bash
curl -X POST http://localhost:8080/api/inventory/warehouses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Main Warehouse",
    "address": "789 Warehouse Blvd, City, State",
    "businessId": 1,
    "isActive": true
  }'
```

### Create Sample Customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "John Customer",
    "phone": "+1-555-0300",
    "email": "john@customer.com",
    "address": "123 Customer St, City, State",
    "businessId": 1
  }'
```

### Create Sample Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "Sample Product",
    "skuCode": "SP-001",
    "category": "Electronics",
    "quantity": 100,
    "costPrice": 25.00,
    "sellingPrice": 50.00,
    "businessId": 1,
    "vendorId": 1,
    "isActive": true
  }'
```

---

# 10. ERROR RESPONSES

## Common Error Response Format
```json
{
  "message": "Error description",
  "success": false,
  "data": null
}
```

## HTTP Status Codes
- **200 OK**: Successful GET, PUT requests
- **201 Created**: Successful POST requests
- **400 Bad Request**: Invalid request data
- **401 Unauthorized**: Missing or invalid authentication
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

## Authentication Errors
```json
{
  "message": "JWT token is expired",
  "success": false,
  "data": null
}
```

## Validation Errors
```json
{
  "message": "Validation failed: Name is required",
  "success": false,
  "data": null
}
```

---

# 11. ENVIRONMENT VARIABLES

Set these environment variables before running the application:

```bash
# Database Configuration
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=stockmanager
export DB_USERNAME=postgres
export DB_PASSWORD=password

# JWT Configuration
export JWT_SECRET=mySecretKey
export JWT_EXPIRATION=86400000

# Server Configuration
export SERVER_PORT=8080
```

---

# 12. POSTMAN COLLECTION

You can import these cURL commands into Postman by:

1. Open Postman
2. Click "Import" 
3. Select "Raw text"
4. Paste any cURL command
5. Click "Continue" and "Import"

Or create a Postman environment with:
- `baseUrl`: `http://localhost:8080`
- `token`: `<your-jwt-token>`
- `businessId`: `1`

Then use `{{baseUrl}}`, `{{token}}`, and `{{businessId}}` in your requests.

---

# 13. API TESTING CHECKLIST

## ✅ Business Management
- [ ] Create business
- [ ] Get business by ID
- [ ] Update business
- [ ] Get all businesses
- [ ] Delete business

## ✅ Product Management  
- [ ] Create product
- [ ] Get product by ID
- [ ] Update product
- [ ] Search products
- [ ] Create variants
- [ ] Create batches
- [ ] Stock operations

## ✅ Sales Management
- [ ] Create sale
- [ ] Add payments
- [ ] Process refunds
- [ ] Get sales analytics
- [ ] Cancel sales

## ✅ Customer Management
- [ ] Create customer
- [ ] Update customer
- [ ] Search customers
- [ ] Get purchase history
- [ ] Loyalty points

## ✅ Inventory Management
- [ ] Create warehouse
- [ ] Stock transfers
- [ ] Stock adjustments
- [ ] Stock counts
- [ ] Low stock alerts

## ✅ Reporting & Analytics
- [ ] Sales reports
- [ ] Inventory reports
- [ ] Customer reports
- [ ] Financial reports
- [ ] Dashboard data
- [ ] Export reports

This comprehensive API documentation provides complete cURL examples for testing all 120+ endpoints in your Stock Manager application. Each section includes realistic sample data and proper authentication headers.
