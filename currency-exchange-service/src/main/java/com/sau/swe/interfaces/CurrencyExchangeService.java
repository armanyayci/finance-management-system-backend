package com.sau.swe.interfaces;

import com.sau.swe.dto.CurrencyConversionResponse;
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Map;

public interface CurrencyExchangeService {
    CurrencyConversionResponse convertCurrency(String fromCurrency, String toCurrency, BigDecimal amount) throws JSONException;

    Map<String, BigDecimal> getAllExchangeRates() throws JSONException;
}
