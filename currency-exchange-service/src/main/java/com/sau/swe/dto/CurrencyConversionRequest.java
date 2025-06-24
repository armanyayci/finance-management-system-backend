package com.sau.swe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionRequest {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;
    private BigDecimal conversionRate;
    private Long userId;
}
