package com.jmdt.stockmanager.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReplacementRecordDTO {
    private String originalSerialNumber;
    private String newSerialNumber;
    private String model;
    private String replacementReason;
    private LocalDate replacedDate;
    private String customerName;
    private String customerMobile;
    private int warrantyInMonths;
}

