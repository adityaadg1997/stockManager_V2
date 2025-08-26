package com.jmdt.stockmanager.service;

import com.jmdt.stockmanager.dto.request.StockDashboardDTO;

import java.util.List;

public interface StockService {
    List<StockDashboardDTO> getLiveStockData();
}

