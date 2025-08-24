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
    public Object data;

    public ApiResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
