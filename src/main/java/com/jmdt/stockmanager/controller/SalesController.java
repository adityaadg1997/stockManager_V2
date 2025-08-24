package com.jmdt.stockmanager.controller;

import com.jmdt.stockmanager.dto.request.SalesRequestDTO;
import com.jmdt.stockmanager.dto.response.SalesResponseDTO;
import com.jmdt.stockmanager.service.SalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @PostMapping("/create-sale")
    public ResponseEntity<?> createSale(@RequestBody SalesRequestDTO dto) {
        try {
            String response = salesService.createSale(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<SalesResponseDTO>> getAllSales() {
        List<SalesResponseDTO> response = salesService.getAllSales();
        return ResponseEntity.ok(response);
    }
}

