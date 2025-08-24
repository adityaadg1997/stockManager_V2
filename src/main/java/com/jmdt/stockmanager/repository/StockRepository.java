package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, String> {
    Optional<Stock> findBySerialNumber(String serialNumber);
}

