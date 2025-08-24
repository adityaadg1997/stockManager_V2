package com.jmdt.stockmanager.service;

import com.jmdt.stockmanager.dto.StockDashboardDTO;

import java.util.List;

public interface StockService {
    List<StockDashboardDTO> getLiveStockData();
}

