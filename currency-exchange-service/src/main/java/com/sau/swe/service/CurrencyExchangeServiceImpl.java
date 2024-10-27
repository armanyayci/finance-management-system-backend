package com.sau.swe.service;

import com.sau.swe.dto.CurrencyConversionResponse;
import com.sau.swe.interfaces.CurrencyExchangeService;
import com.sau.swe.repository.CurrencyExchangeRepository;
import com.sau.swe.utils.exception.CurrencyExchangeException;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CURRENCY_API_URL = "https://finans.truncgil.com/today.json";

    private final CurrencyExchangeRepository currencyExchangeRepository;

    @Override
    public CurrencyConversionResponse convertCurrency(String fromCurrency, String toCurrency, BigDecimal amount) throws JSONException {
        BigDecimal exchangeRate = getExchangeRate(fromCurrency, toCurrency);

        if (exchangeRate == null) {
            throw new CurrencyExchangeException("Döviz kuru bulunamadı: " + fromCurrency + " -> " + toCurrency);
        }

        BigDecimal convertedAmount = amount.multiply(exchangeRate);

        CurrencyConversionResponse response = new CurrencyConversionResponse();
        response.setFromCurrency(fromCurrency);
        response.setToCurrency(toCurrency);
        response.setOriginalAmount(amount);
        response.setConvertedAmount(convertedAmount);
        response.setExchangeRate(exchangeRate);

        return response;
    }

    @Override
    public Map<String, BigDecimal> getAllExchangeRates() throws JSONException {
        String response = restTemplate.getForObject(CURRENCY_API_URL, String.class);
        JSONObject jsonObject = new JSONObject(response);

        Map<String, BigDecimal> exchangeRates = new HashMap<>();

        jsonObject.keys().forEachRemaining(currencyCode -> {
            JSONObject currencyInfo = null;
            try {
                currencyInfo = jsonObject.getJSONObject(currencyCode.toString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String rateString = null;
            try {
                rateString = currencyInfo.getString("Alış").replace(",", ".");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            BigDecimal exchangeRate = new BigDecimal(rateString);
            exchangeRates.put(currencyCode.toString(), exchangeRate);
        });

        return exchangeRates;
    }

    // API'den iki döviz arasında kuru alma
    private BigDecimal getExchangeRate(String fromCurrency, String toCurrency) throws JSONException {
        String response = restTemplate.getForObject(CURRENCY_API_URL, String.class);
        JSONObject jsonObject = new JSONObject(response);

        try {
            if (fromCurrency.equals("USD") && toCurrency.equals("TRY")) {
                // USD -> TRY kuru
                String rateString = jsonObject.getJSONObject("USD").getString("Alış");
                return new BigDecimal(rateString.replace(",", "."));
            } else if (fromCurrency.equals("EUR") && toCurrency.equals("TRY")) {
                // EUR -> TRY kuru
                String rateString = jsonObject.getJSONObject("EUR").getString("Alış");
                return new BigDecimal(rateString.replace(",", "."));
            }
        } catch (Exception e) {
            throw new CurrencyExchangeException("Kur bilgisi alınırken hata oluştu: " + e.getMessage());
        }
        return null;
    }
}
