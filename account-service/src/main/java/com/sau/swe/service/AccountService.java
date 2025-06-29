package com.sau.swe.service;

import com.sau.swe.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface AccountService {

    void createAccount(CreateAccountDTO dto);


    void moneyTransfer(TransferRequest request);

    void addBalance(BalanceRequest balanceRequest);

    List<AccountResponse> getAccountByUsername(String username);

    List<TransactionDTO> expenseAnalyze(String username, LocalDateTime startDate, LocalDateTime endDate);
}
