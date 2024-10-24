package com.sau.swe.api;

import com.sau.swe.dto.CreateAccountDTO;
import com.sau.swe.service.AccountService;
import com.sau.swe.utils.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    @PostMapping("/add-account")
    public GenericResponse<String> createAccount(@RequestBody CreateAccountDTO dto){
        accountService.createAccount(dto);
        return GenericResponse.success("generic.account.created");
    }
}
