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
@Table(name = "daily_sales_snapshots", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"business_id", "snapshot_date"}))
public class DailySalesSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate;

    @Column(name = "total_sales", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalSales;

    @Column(name = "total_orders", nullable = false)
    private Integer totalOrders;

    @Column(name = "avg_order_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal avgOrderValue;

    @Column(name = "refunds_amount", precision = 10, scale = 2)
    private BigDecimal refundsAmount = BigDecimal.ZERO;
}
