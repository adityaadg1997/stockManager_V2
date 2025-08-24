package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.Sales;
import com.jmdt.stockmanager.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, String> {
    List<Sales> findByStock(Stock stock);

}

