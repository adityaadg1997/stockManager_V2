package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findBySaleId(Long saleId);
    
    List<Payment> findByCustomerId(Long customerId);
    
    @Query("SELECT COALESCE(SUM(p.amountPaid), 0) FROM Payment p WHERE p.sale.id = :saleId")
    BigDecimal getTotalPaidAmountBySaleId(@Param("saleId") Long saleId);
    
    @Query("SELECT p FROM Payment p WHERE p.sale.id = :saleId ORDER BY p.paymentDate DESC")
    List<Payment> findBySaleIdOrderByPaymentDateDesc(@Param("saleId") Long saleId);
    
    @Query("SELECT p FROM Payment p WHERE p.customer.id = :customerId ORDER BY p.paymentDate DESC")
    List<Payment> findByCustomerIdOrderByPaymentDateDesc(@Param("customerId") Long customerId);
}
