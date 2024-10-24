package com.sau.swe.service;

import com.sau.swe.dto.CreateAccountDTO;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    void createAccount(CreateAccountDTO dto);


}
