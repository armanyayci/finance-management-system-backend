package com.sau.swe.api;

import com.sau.swe.dto.*;
import com.sau.swe.service.AccountService;
import com.sau.swe.utils.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    @PostMapping("/add-account")
    public GenericResponse<String> createAccount(@RequestBody CreateAccountDTO dto){
        accountService.createAccount(dto);
        return GenericResponse.success("generic.account.created");
    }
    @PostMapping("/money-transfer")
    public GenericResponse<String> moneyTransfer(@RequestBody TransferRequest request){
        accountService.moneyTransfer(request);
        return GenericResponse.success("generic.account.money.transfered");
    }
    @PostMapping("/add-balance")
    public GenericResponse<String> addBalance(@RequestBody BalanceRequest balanceRequest){
        accountService.addBalance(balanceRequest);
        return GenericResponse.success("generic.account.balance");
    }
    @GetMapping("/get-account/{username}")
    public GenericResponse<AccountResponse> getAccountByUsername(@PathVariable("username") String username){
        AccountResponse accountResponse=accountService.getAccountByUsername(username);
        return GenericResponse.success(accountResponse);
    }

}
