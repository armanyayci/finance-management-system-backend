package com.sau.swe.service;

import com.sau.swe.dto.CurrencyConversionRequest;
import com.sau.swe.dto.CurrencyListDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CurrencyExchangeService {
    void buy(CurrencyConversionRequest conversionRequest);

    void sellCurrency(CurrencyConversionRequest conversionRequest);

    List<CurrencyListDTO> getUserCurrencies(Long userId);
}
