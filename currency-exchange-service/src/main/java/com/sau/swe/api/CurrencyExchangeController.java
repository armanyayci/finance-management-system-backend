package com.sau.swe.api;

import com.sau.swe.dto.CurrencyConversionRequest;
import com.sau.swe.dto.CurrencyConversionResponse;
import com.sau.swe.dto.CurrencyListDTO;
import com.sau.swe.service.CurrencyExchangeService;
import com.sau.swe.utils.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/currency-exchange")
public class CurrencyExchangeController {

    private final CurrencyExchangeService currencyExchangeService;

    @PostMapping("/buy")
    public GenericResponse<CurrencyConversionResponse> exchangeCurrency(@RequestBody CurrencyConversionRequest conversionRequest) {
        currencyExchangeService.buy(conversionRequest);
        return GenericResponse.success("currency.success");
    }

    @PostMapping("/sell")
    public GenericResponse<List<Void>> sellCurrency(@RequestBody CurrencyConversionRequest conversionRequest) {
        currencyExchangeService.sellCurrency(conversionRequest);
        return GenericResponse.success("currency.success");
    }

    @GetMapping("/get-user-currencies")
    public GenericResponse<List<CurrencyListDTO>> getUserCurrencies(@RequestParam("userId") Long userId) {
        return GenericResponse.success(currencyExchangeService.getUserCurrencies(userId));
    }

}
