package com.sau.swe.dto;

import com.sau.swe.entity.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
public class AccountResponse {
    private BigDecimal balance;
    private String accountType;
    private String transferCode;
    private List<TransactionDTO> lastTransactions;

    public AccountResponse(BigDecimal balance, Integer accountType, String transferCode) {
        this.balance = balance;
        this.accountType = Account.AccountType.values()[accountType].name();
        this.transferCode = transferCode;
    }
}
