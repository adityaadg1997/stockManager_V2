package com.jmdt.stockmanager.service.impl;

import com.jmdt.stockmanager.dto.request.ReplacementRecordDTO;
import com.jmdt.stockmanager.exception.ResourceNotFoundException;
import com.jmdt.stockmanager.models.ReplacementRecord;
import com.jmdt.stockmanager.models.Stock;
import com.jmdt.stockmanager.repository.ReplacementRecordRepository;
import com.jmdt.stockmanager.repository.StockRepository;
import com.jmdt.stockmanager.service.ReplacementService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReplacementServiceImpl implements ReplacementService {

    @Autowired
    private ReplacementRecordRepository replacementRepo;

    @Autowired
    private StockRepository stockRepository;

    @Override
    public void recordReplacement(ReplacementRecordDTO request) {
        Stock originalStock = stockRepository.findBySerialNumber(request.getOriginalSerialNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "originalSerialNumber", request.getOriginalSerialNumber()));

        // Set stock as replaced
        originalStock.setReplaced(true);
        stockRepository.save(originalStock);

        // Save replacement record
        ReplacementRecord record = new ReplacementRecord();
        String randomId = UUID.randomUUID().toString();
        record.setId(randomId);
        record.setOriginalStock(originalStock);
        record.setOriginalSerialNumber(request.getOriginalSerialNumber());
        record.setNewSerialNumber(request.getNewSerialNumber());
        record.setReplacementReason(request.getReplacementReason());

        record.setCustomerName(request.getCustomerName());
        record.setCustomerMobile(request.getCustomerMobile());

        LocalDate localDate = Objects.nonNull(request.getReplacedDate()) ? request.getReplacedDate() : LocalDate.now();
        record.setReplacedDate(localDate);
        replacementRepo.save(record);
    }

    @Override
    public List<ReplacementRecordDTO> getBySerial(String serial) {
        return replacementRepo.findByOriginalSerialNumber(serial).stream()
                .map(r -> {
                    ReplacementRecordDTO dto = new ReplacementRecordDTO();
                    BeanUtils.copyProperties(r, dto);
                    return dto;
                }).collect(Collectors.toList());
    }
}

