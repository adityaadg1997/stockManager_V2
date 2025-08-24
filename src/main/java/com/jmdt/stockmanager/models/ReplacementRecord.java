package com.jmdt.stockmanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class ReplacementRecord {

    @Id
    private String id;

    private String originalSerialNumber;
    private String newSerialNumber;
    private String model;
    private String replacementReason;
    private LocalDate replacedDate;

    private String customerName;
    private String customerMobile;

    private int warrantyInMonths;

    // Optional: link to original stock if needed
    @ManyToOne
    @JoinColumn(name = "original_stock_id")
    private Stock originalStock;

}

