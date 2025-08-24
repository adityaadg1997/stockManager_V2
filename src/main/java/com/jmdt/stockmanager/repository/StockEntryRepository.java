package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockEntryRepository extends JpaRepository<Stock, String> {
    List<Stock> findByIsSoldFalse();
}

