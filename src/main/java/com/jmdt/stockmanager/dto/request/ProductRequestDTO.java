package com.jmdt.stockmanager.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {
    private String name;
    private String skuCode;
    private String category;
    private Integer quantity;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private String imageUrl;
    private Boolean isActive;
    private Boolean hasWarranty;
    private Integer warrantyDurationDays;
    private Boolean batchTracked;
    private Long businessId;
    private Long vendorId;
}
