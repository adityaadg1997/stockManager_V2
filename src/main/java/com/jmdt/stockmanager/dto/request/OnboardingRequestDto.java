package com.jmdt.stockmanager.dto.request;

import lombok.Data;

@Data
public class OnboardingRequestDto {
    private String businessName;
    private String businessEmail;
    private String adminName;
    private String adminEmail;
    private String adminPassword;
    // Add other fields as needed
}
