package com.jmdt.stockmanager.service;

import com.jmdt.stockmanager.dto.request.ReplacementRecordDTO;

import java.util.List;

public interface ReplacementService {

    void recordReplacement(ReplacementRecordDTO dto);

    List<ReplacementRecordDTO> getBySerial(String serial);
}
