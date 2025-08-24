package com.jmdt.stockmanager.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SalesResponseDTO {

    private String model;
    private String serialNumber;
    private int quantity;
    private String customerName;
    private String customerMobile;
    private LocalDate soldDate;

    private String productName;
}

