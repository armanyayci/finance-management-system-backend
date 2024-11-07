package com.sau.swe.controller;

import com.sau.swe.dto.LoginDto;
import com.sau.swe.dto.SignUpDto;
import com.sau.swe.dto.TokenResponse;
import com.sau.swe.service.Abstract.AuthenticationService;
import com.sau.swe.utils.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/authenticate")
public class AuthenticationController {

   private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public GenericResponse<String> signup(@RequestBody SignUpDto sign) {
        authenticationService.signup(sign);
        return GenericResponse.success("generic.account.created");
    }


    @PostMapping("/login")
    public GenericResponse<TokenResponse> login(@RequestBody LoginDto loginDto){
        TokenResponse tokenResponse = authenticationService.login(loginDto);
        return GenericResponse.success(tokenResponse);

    }
}
