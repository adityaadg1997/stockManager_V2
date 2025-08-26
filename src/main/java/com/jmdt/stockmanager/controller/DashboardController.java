package com.jmdt.stockmanager.controller;

import com.jmdt.stockmanager.dto.request.StockDashboardDTO;
import com.jmdt.stockmanager.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private StockService stockService;

    @GetMapping("/stock")
    public ResponseEntity<List<StockDashboardDTO>> getLiveStock() {
        List<StockDashboardDTO> stockList = stockService.getLiveStockData();
        return ResponseEntity.ok(stockList);
    }
}
