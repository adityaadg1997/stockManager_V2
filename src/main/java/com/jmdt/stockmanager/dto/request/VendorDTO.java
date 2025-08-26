package com.jmdt.stockmanager.dto.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorDTO {
    private Long id;
    private String name;
    private String contactEmail;
    private String phone;
    private String address;
    private String paymentTerms;
    private Long businessId;
}
