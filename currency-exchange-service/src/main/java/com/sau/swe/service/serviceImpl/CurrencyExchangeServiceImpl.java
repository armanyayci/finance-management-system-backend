package com.sau.swe.service.serviceImpl;

import com.sau.swe.dao.*;
import com.sau.swe.dto.CurrencyConversionRequest;
import com.sau.swe.dto.CurrencyListDTO;
import com.sau.swe.entity.*;
import com.sau.swe.service.CurrencyExchangeService;
import com.sau.swe.utils.exception.GenericFinanceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CURRENCY_API_URL = "https://finans.truncgil.com/today.json";

    private final CurrencyExchangeRepository currencyExchangeRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountActivitiesRepository accountActivitiesRepository;
    private final AccountCurrencyRepository accountCurrencyRepository;

    @Override
    @Transactional
    public void buy(CurrencyConversionRequest conversionRequest) {
        log.info("Buy currency conversion started. UserId: {}", conversionRequest.getUserId());

        BigDecimal totalExchange = conversionRequest.getAmount().multiply(conversionRequest.getConversionRate());
        Optional<Account> accountOpt = accountRepository.getAccountByUserId(conversionRequest.getUserId(), Account.AccountType.TRY);
        if (accountOpt.isEmpty()) {
            throw new GenericFinanceException("account.notfound");
        }
        Account account = accountOpt.get();
        BigDecimal userAccBalance = account.getBalance();
        if (userAccBalance.compareTo(totalExchange) < 0) {
            throw new GenericFinanceException("currency.not.enough.balance");
        }

        LocalDateTime currencyDateTime = LocalDateTime.now();

        Transaction transaction = Transaction.builder()
                .transactionTime(currencyDateTime)
                .amount(totalExchange)
                .category("EXCHANGE")
                .paymentType(Transaction.PaymentType.EXCHANGE)
                .build();
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Currency Exchange - Buy - Saved transaction ID: {}", savedTransaction.getId());

        AccountActivities accountActivities = AccountActivities.builder()
                .isIncome(false)
                .account(account)
                .description(String.format("CURRENCY EXCHANGE ORDER. Bought %s at rate %.2f for amount %.2f, Total Price: %s %s",
                        conversionRequest.getToCurrency(), conversionRequest.getConversionRate(), conversionRequest.getAmount(), totalExchange, conversionRequest.getFromCurrency()) )
                .transaction(transaction)
                .build();
        accountActivitiesRepository.save(accountActivities);
        log.info("Currency Exchange - Buy - Saved AccountActivities ID: {}", accountActivities.getId());

        CurrencyExchange exchange = CurrencyExchange.builder()
                .fromCurrency(conversionRequest.getFromCurrency())
                .toCurrency(conversionRequest.getToCurrency())
                .exchangeRate(conversionRequest.getConversionRate())
                .exchangeDate(currencyDateTime)
                .transactionId(savedTransaction.getId())
                .build();
        currencyExchangeRepository.save(exchange);
        log.info("Currency Exchange - Buy - Saved CurrencyExchange ID: {} ", exchange.getId());

        AccountCurrency accountCurrency;
        Optional<AccountCurrency> currentAccountCurrency = accountCurrencyRepository.getAccountCurrencyWithUserIdAndCurrencyName(
                conversionRequest.getToCurrency(), conversionRequest.getUserId());

        if (currentAccountCurrency.isPresent()) {
            accountCurrency = currentAccountCurrency.get();
            BigDecimal newAmount = accountCurrency.getAmount().add(conversionRequest.getAmount());
            accountCurrency.setAmount(newAmount);
            accountCurrencyRepository.save(accountCurrency);
            log.info("Currency Exchange - Buy - AccountCurrency amount updated to {}. ID: " , accountCurrency.getId());
        }
        else {
            accountCurrency = AccountCurrency.builder()
                    .currencyName(conversionRequest.getToCurrency())
                    .amount(conversionRequest.getAmount())
                    .account(account)
                    .build();
            AccountCurrency savedAccountCurrency = accountCurrencyRepository.save(accountCurrency);
            log.info("Currency Exchange - Buy - Saved new AccountCurrency ID: {}", savedAccountCurrency.getId());
        }

        account.setBalance(userAccBalance.subtract(totalExchange));
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void sellCurrency(CurrencyConversionRequest conversionRequest) {

        Optional<AccountCurrency> accountCurrencyOpt = accountCurrencyRepository.getAccountCurrencyWithUserIdAndCurrencyName(
                conversionRequest.getFromCurrency(),conversionRequest.getUserId());
        Optional<Account> accountOpt = accountRepository.getAccountByUserId(conversionRequest.getUserId(),Account.AccountType.TRY);

        if (accountCurrencyOpt.isEmpty()){
            throw new GenericFinanceException("currency.accountcurrency.notfound");
        }
        AccountCurrency accountCurrency = accountCurrencyOpt.get();
        BigDecimal totalExchange = conversionRequest.getAmount().multiply(conversionRequest.getConversionRate());

        if (accountCurrency.getAmount().compareTo(conversionRequest.getAmount()) < 0) {
            throw new GenericFinanceException("currency.sell.not.enough.amount");
        }
        if (accountOpt.isEmpty()) {
            throw new GenericFinanceException("account.notfound");
        }
        Account account = accountOpt.get();
        LocalDateTime currencyDateTime = LocalDateTime.now();

        Transaction transaction = Transaction.builder()
                .transactionTime(currencyDateTime)
                .amount(totalExchange)
                .category("EXCHANGE")
                .paymentType(Transaction.PaymentType.EXCHANGE)
                .build();
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Currency Exchange - Sell - Saved transaction ID: {}", savedTransaction.getId());

        AccountActivities accountActivities = AccountActivities.builder()
                .isIncome(true)
                .account(account)
                .description(String.format("CURRENCY EXCHANGE ORDER. Sold %s at rate %.2f for amount %.2f, Total Price: %s %s",
                        conversionRequest.getFromCurrency(), conversionRequest.getConversionRate(), conversionRequest.getAmount(), totalExchange, conversionRequest.getToCurrency()) )
                .transaction(transaction)
                .build();
        accountActivitiesRepository.save(accountActivities);
        log.info("Currency Exchange - Sell - Saved AccountActivities ID: {}", accountActivities.getId());

        CurrencyExchange exchange = CurrencyExchange.builder()
                .fromCurrency(conversionRequest.getFromCurrency())
                .toCurrency(conversionRequest.getToCurrency())
                .exchangeRate(conversionRequest.getConversionRate())
                .exchangeDate(currencyDateTime)
                .transactionId(savedTransaction.getId())
                .build();
        currencyExchangeRepository.save(exchange);

        if (accountCurrency.getAmount().compareTo(conversionRequest.getAmount()) == 0) {
            accountCurrencyRepository.delete(accountCurrency);
        }
        else {
            BigDecimal newCurrencySize = accountCurrency.getAmount().subtract(conversionRequest.getAmount());
            accountCurrency.setAmount(newCurrencySize);
            accountCurrencyRepository.save(accountCurrency);
        }
        BigDecimal oldBalance = account.getBalance();
        account.setBalance(oldBalance.add(totalExchange));
        accountRepository.save(account);
    }

    @Override
    public List<CurrencyListDTO> getUserCurrencies(Long userId) {
        return accountCurrencyRepository.getUserCurrencies(userId);
    }

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
}
