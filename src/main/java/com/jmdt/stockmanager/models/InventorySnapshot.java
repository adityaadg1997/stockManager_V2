package com.jmdt.stockmanager.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory_snapshots", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "snapshot_date"}))
public class InventorySnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "cost_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal costValue;

    @Column(name = "selling_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal sellingValue;
}
