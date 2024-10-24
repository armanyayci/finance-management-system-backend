package com.sau.swe.service.Abstract;

import com.sau.swe.dto.LoginDto;
import com.sau.swe.dto.SignUpDto;
import com.sau.swe.dto.TokenResponse;

public interface AuthenticationService {

    TokenResponse login(LoginDto loginDto);

    void signup(SignUpDto sign);
}
