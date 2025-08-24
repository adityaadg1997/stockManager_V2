package com.jmdt.stockmanager.controller;

import com.jmdt.stockmanager.dto.ReplacementRecordDTO;
import com.jmdt.stockmanager.service.ReplacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/replacements")
public class ReplacementController {

    @Autowired
    private ReplacementService replacementService;

    @PostMapping("/")
    public ResponseEntity<String> replaceProduct(@RequestBody ReplacementRecordDTO dto) {
        replacementService.recordReplacement(dto);
        return ResponseEntity.ok("Replacement recorded successfully.");
    }

    @GetMapping("/{serial}")
    public ResponseEntity<List<ReplacementRecordDTO>> getReplacements(@PathVariable String serial) {
        return ResponseEntity.ok(replacementService.getBySerial(serial));
    }
}

