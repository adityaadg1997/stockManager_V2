package com.jmdt.stockmanager.dto.request;

import lombok.Data;

@Data
public class SalesRequestDTO {

    private String stockId;
    private int quantity;
    private String customerName;
    private String customerAddress;
    private String customerMobile;

    private String productName;
}

