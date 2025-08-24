package com.jmdt.stockmanager.payloads;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    public String message;
    public boolean status;

}
