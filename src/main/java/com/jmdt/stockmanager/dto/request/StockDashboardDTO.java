package com.jmdt.stockmanager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockDashboardDTO {

    private String productName;
    private String model;
    private String serialNumber;
    private Double currentQuantity;
    private String unit;
    private String status; // e.g., "Available", "Low Stock", "Out of Stock"

    private boolean replaced;
    private LocalDate replacedDate;
    private String replacementReason;
    private String newSerialNumber;

    private boolean sold;
    private LocalDate soldDate;
    private String customerName;
    private String customerMobile;

    private int warrantyInMonths;

}

