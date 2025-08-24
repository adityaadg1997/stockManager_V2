package com.jmdt.stockmanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Stock {

    @Id
    private String id;

    private String productName;
    private String model;
    private String serialNumber;
    private String unitType;     // Kg / Ft / Pcs
    private Double quantity;
    private LocalDate entryDate;

    // Optional fields for future use
    private boolean isSold = false;

    private boolean replaced;
}

