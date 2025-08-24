package com.jmdt.stockmanager.service.impl;

import com.jmdt.stockmanager.models.Stock;
import com.jmdt.stockmanager.repository.StockEntryRepository;
import com.jmdt.stockmanager.service.StockEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class StockEntryServiceImpl implements StockEntryService {

    @Autowired
    private StockEntryRepository stockRepo;

    public Stock addStock(Stock stock) {
        String randomId = UUID.randomUUID().toString();
        stock.setId(randomId);
        stock.setEntryDate(LocalDate.now());
        return stockRepo.save(stock);
    }

    public List<Stock> getAvailableStock() {
        return stockRepo.findByIsSoldFalse();
    }

    @Override
    public List<Stock> getAllStock() {
        return stockRepo.findAll();
    }

}
