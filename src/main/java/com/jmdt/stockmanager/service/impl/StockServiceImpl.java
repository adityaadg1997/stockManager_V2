package com.jmdt.stockmanager.service.impl;

import com.jmdt.stockmanager.dto.request.StockDashboardDTO;
import com.jmdt.stockmanager.models.ReplacementRecord;
import com.jmdt.stockmanager.models.Sales;
import com.jmdt.stockmanager.models.Stock;
import com.jmdt.stockmanager.repository.ReplacementRecordRepository;
import com.jmdt.stockmanager.repository.SalesRepository;
import com.jmdt.stockmanager.repository.StockRepository;
import com.jmdt.stockmanager.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.jmdt.stockmanager.constants.AppConstants.LIFETIME_WARRANTY;

@Slf4j
@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private ReplacementRecordRepository replacementRepo;

    @Override
    public List<StockDashboardDTO> getLiveStockData() {
        List<Stock> stocks = stockRepository.findAll();

        return stocks.stream().map(stock -> {
            String status;
            Double qty = stock.getQuantity();

            if (qty == 0) {
                status = "Out of Stock";
            } else if (qty < 5) {
                status = "Low Stock";
            } else {
                status = "Available";
            }

            // Start building DTO
            StockDashboardDTO dto = new StockDashboardDTO();
            dto.setProductName(stock.getProductName());
            dto.setModel(stock.getModel());
            dto.setSerialNumber(stock.getSerialNumber());
            dto.setCurrentQuantity(qty);
            dto.setUnit(stock.getUnitType());
            dto.setStatus(status);

            //Check if stock has any associated Sales
            List<Sales> sales = salesRepository.findByStock(stock);
            if (!sales.isEmpty()) {
                // We can show the most recent sale or first sale — your choice
                Sales latestSale = sales.get(0);
                dto.setSold(true);
                dto.setSoldDate(latestSale.getSoldDate());
                dto.setCustomerName(latestSale.getCustomerName());
                dto.setCustomerMobile(latestSale.getCustomerMobile());
            } else {
                dto.setSold(false);
            }

            // Replacement Check
            List<ReplacementRecord> replacements = replacementRepo.findByOriginalSerialNumber(stock.getSerialNumber());
            if (!replacements.isEmpty()) {
                ReplacementRecord replacement = replacements.get(0);
                dto.setReplaced(true);
                dto.setReplacedDate(replacement.getReplacedDate());
                dto.setReplacementReason(replacement.getReplacementReason());
                dto.setNewSerialNumber(replacement.getNewSerialNumber());
            } else {
                dto.setReplaced(false);
            }

            // Warranty
            dto.setWarrantyInMonths(LIFETIME_WARRANTY);

            return dto;
        }).collect(Collectors.toList());
    }

}
