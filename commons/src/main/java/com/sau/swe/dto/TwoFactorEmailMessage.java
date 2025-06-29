package com.sau.swe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorEmailMessage implements Serializable {
    private String email;
    private String username;
    private String code;
} 