package com.jmdt.stockmanager.models;

import com.jmdt.stockmanager.enums.RMAStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vendor_rma")
public class VendorRMA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(name = "rma_number", unique = true, nullable = false)
    private String rmaNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RMAStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiated_by")
    private User initiatedBy;

    @Column(name = "initiated_at", nullable = false)
    private LocalDateTime initiatedAt = LocalDateTime.now();

    @Column(name = "expected_reimbursement", precision = 10, scale = 2)
    private BigDecimal expectedReimbursement;

    @Column(name = "actual_reimbursement", precision = 10, scale = 2)
    private BigDecimal actualReimbursement;

    @Column(name = "reimbursement_date")
    private LocalDateTime reimbursementDate;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Relationships
    @OneToMany(mappedBy = "vendorRMA", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RMAItem> rmaItems;
}
