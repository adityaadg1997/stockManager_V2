package com.jmdt.stockmanager.controller;

import com.jmdt.stockmanager.models.Stock;
import com.jmdt.stockmanager.service.StockEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockEntryController {

    @Autowired
    private StockEntryService stockService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Stock> addNewStock(@RequestBody Stock entry) {
        Stock saved = stockService.addStock(entry);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Stock>> getStock() {
        return ResponseEntity.ok(stockService.getAvailableStock());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Stock>> getAllStock() {
        return ResponseEntity.ok(stockService.getAllStock());
    }

}

