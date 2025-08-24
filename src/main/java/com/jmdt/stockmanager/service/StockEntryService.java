package com.jmdt.stockmanager.service;

import com.jmdt.stockmanager.models.Stock;

import java.util.List;

public interface StockEntryService {

    Stock addStock(Stock stock);

    List<Stock> getAvailableStock();

    List<Stock> getAllStock();

}
