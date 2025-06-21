package com.sau.swe.api;

import com.sau.swe.dto.CurrencyConversionRequest;
import com.sau.swe.dto.CurrencyConversionResponse;
import com.sau.swe.interfaces.CurrencyExchangeService;
import com.sau.swe.utils.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/currency-exchange")
public class CurrencyExchangeController {

    private final CurrencyExchangeService currencyExchangeService;
    @PostMapping(name = "/convert")
    public GenericResponse<CurrencyConversionResponse> convertCurrency(@RequestBody CurrencyConversionRequest conversionRequest) throws JSONException {
        CurrencyConversionResponse response = currencyExchangeService.convertCurrency(
                conversionRequest.getFromCurrency(),
                conversionRequest.getToCurrency(),
                conversionRequest.getAmount()
        );
        return GenericResponse.success(response, "generic.currency.converted");
    }

    @GetMapping(name = "/exchange-rates")
    public GenericResponse<Map<String, BigDecimal>> getAllExchangeRates() throws JSONException {
        Map<String, BigDecimal> exchangeRates = currencyExchangeService.getAllExchangeRates();
        return GenericResponse.success(exchangeRates, "generic.all-exchange-rates.taken");
    }
}
