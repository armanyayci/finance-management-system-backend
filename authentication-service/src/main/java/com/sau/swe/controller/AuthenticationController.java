package com.sau.swe.controller;

import com.sau.swe.dto.LoginDto;
import com.sau.swe.dto.SignUpDto;
import com.sau.swe.dto.TokenResponse;
import com.sau.swe.service.Abstract.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

   private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpDto sign) {
        authenticationService.signup(sign);
        return ResponseEntity.ok("User Created Successfully");
    }


    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginDto loginDto){
        TokenResponse tokenResponse = authenticationService.login(loginDto);
        return tokenResponse;

    }
}
