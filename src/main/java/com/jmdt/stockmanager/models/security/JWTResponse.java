package com.jmdt.stockmanager.models.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JWTResponse {

    private String token;
    private String username;
}
