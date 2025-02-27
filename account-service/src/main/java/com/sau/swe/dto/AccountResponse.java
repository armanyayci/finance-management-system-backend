package com.sau.swe.dto;

import com.sau.swe.entity.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AccountResponse {
    private Long balance;
    private String accountType;
    private String transferCode;

    public AccountResponse(Long balance, Integer accountType, String transferCode) {
        this.balance = balance;
        this.accountType = Account.AccountType.values()[accountType].name();
        this.transferCode = transferCode;
    }
}
