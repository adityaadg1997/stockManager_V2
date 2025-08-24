package com.jmdt.stockmanager.repository;

import com.jmdt.stockmanager.models.ReplacementRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplacementRecordRepository extends JpaRepository<ReplacementRecord, String> {
    List<ReplacementRecord> findByOriginalSerialNumber(String originalSerialNumber);
}

