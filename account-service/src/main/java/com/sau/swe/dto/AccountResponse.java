package com.sau.swe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountResponse {
    private Long balance;
    private String accountType;
    private String transferCode;
}
