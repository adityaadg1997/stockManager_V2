package com.jmdt.stockmanager.service.impl;

import com.jmdt.stockmanager.dto.request.SalesRequestDTO;
import com.jmdt.stockmanager.dto.response.SalesResponseDTO;
import com.jmdt.stockmanager.exception.StockManagerException;
import com.jmdt.stockmanager.models.Sales;
import com.jmdt.stockmanager.models.Stock;
import com.jmdt.stockmanager.repository.SalesRepository;
import com.jmdt.stockmanager.repository.StockEntryRepository;
import com.jmdt.stockmanager.service.SalesService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SalesServiceImpl implements SalesService {

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private StockEntryRepository stockRepository;

    @Transactional
    @Override
    public String createSale(SalesRequestDTO dto) {
        Optional<Stock> stockOpt = stockRepository.findById(dto.getStockId());
        if (stockOpt.isEmpty()) {
            throw new StockManagerException("Stock not found");
        }

        Stock stock = stockOpt.get();

        if (dto.getQuantity() > stock.getQuantity()) {
            throw new StockManagerException("Insufficient stock available");
        }

        // Update stock quantity
        stock.setQuantity(stock.getQuantity() - dto.getQuantity());
        stockRepository.save(stock);

        // Save sale
        Sales sale = new Sales();
        String randomId = UUID.randomUUID().toString();
        sale.setId(randomId);
        sale.setStock(stock);
        sale.setQuantity(dto.getQuantity());
        sale.setCustomerName(dto.getCustomerName());
        sale.setCustomerAddress(dto.getCustomerAddress());
        sale.setCustomerMobile(dto.getCustomerMobile());
        sale.setSoldDate(LocalDate.now());
        sale.setProductName(dto.getProductName());
        salesRepository.save(sale);

        return "Sale recorded successfully";
    }

    @Override
    public List<SalesResponseDTO> getAllSales() {
        List<Sales> salesList = salesRepository.findAll();

        return salesList.stream().map(s -> {
            SalesResponseDTO dto = new SalesResponseDTO();
            dto.setModel(s.getStock().getModel());
            dto.setSerialNumber(s.getStock().getSerialNumber());
            dto.setQuantity(s.getQuantity());
            dto.setCustomerName(s.getCustomerName());
            dto.setCustomerMobile(s.getCustomerMobile());
            dto.setSoldDate(s.getSoldDate());
            dto.setProductName(s.getProductName());
            return dto;
        }).collect(Collectors.toList());
    }
}

