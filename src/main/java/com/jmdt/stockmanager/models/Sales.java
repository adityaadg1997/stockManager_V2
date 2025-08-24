package com.jmdt.stockmanager.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "sales")
public class Sales {
    @Id
    private String id;

    private int quantity;

    private String customerName;
    private String customerAddress;
    private String customerMobile;
    private LocalDate soldDate;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    private String productName;
}

