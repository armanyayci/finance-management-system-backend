package com.sau.swe.controller;

import com.sau.swe.dto.*;
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
    public GenericResponse<TwoFactorLoginResponse> login(@RequestBody LoginDto loginDto){
        TwoFactorLoginResponse response = authenticationService.login(loginDto);
        return GenericResponse.success(response);

    }

    @PostMapping("/verify-2fa")
    public GenericResponse<TokenResponse> verifyTwoFactorCode(@RequestBody TwoFactorLoginRequest request){
        TokenResponse tokenResponse = authenticationService.verifyTwoFactorCode(request);
        return GenericResponse.success(tokenResponse);
    }

    @PostMapping("/change-password")
    public GenericResponse<Void> changePassword(@RequestBody PasswordChangeRequest request){
        authenticationService.changePassword(request);
        return GenericResponse.success("user.profile.password.changed");

    }

    @PostMapping("/enable-2fa")
    public GenericResponse<String> enable2FA(@RequestBody Enable2FARequest request){
        authenticationService.enable2FA(request);
        String message = request.getEnable2FA() ? "auth.2fa.enabled" : "auth.2fa.disabled";
        return GenericResponse.success(message);
    }
}
