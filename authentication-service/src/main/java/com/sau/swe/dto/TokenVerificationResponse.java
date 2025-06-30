package com.sau.swe.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class TokenVerificationResponse {
    private boolean valid;
    private String username;
    private Long userId;
} 