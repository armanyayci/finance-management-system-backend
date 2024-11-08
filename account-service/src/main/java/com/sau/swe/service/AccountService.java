package com.sau.swe.service;

import com.sau.swe.dto.BalanceRequest;
import com.sau.swe.dto.CreateAccountDTO;
import com.sau.swe.dto.TransferRequest;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    void createAccount(CreateAccountDTO dto);


    void moneyTransfer(TransferRequest request);

    void addBalance(BalanceRequest balanceRequest);
}
