package com.jmdt.stockmanager.service;

import com.jmdt.stockmanager.dto.request.SalesRequestDTO;
import com.jmdt.stockmanager.dto.response.SalesResponseDTO;

import java.util.List;

public interface SalesService {

    String createSale(SalesRequestDTO dto);

    List<SalesResponseDTO> getAllSales();
}

