package com.sau.swe.service;

import com.sau.swe.dto.CurrencyConversionRequest;
import org.springframework.stereotype.Service;

@Service
public interface CurrencyExchangeService {
    void buy(CurrencyConversionRequest conversionRequest);
}
