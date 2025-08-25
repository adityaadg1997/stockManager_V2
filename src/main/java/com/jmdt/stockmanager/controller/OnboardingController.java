package com.jmdt.stockmanager.controller;

import com.jmdt.stockmanager.dto.request.OnboardingRequestDto;
import com.jmdt.stockmanager.service.OnboardingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {
    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody OnboardingRequestDto dto) {
        onboardingService.register(dto);
        return ResponseEntity.ok().build();
    }
}
