package com.sau.swe.service.Abstract;

import com.sau.swe.dto.*;

public interface AuthenticationService {

    TwoFactorLoginResponse login(LoginDto loginDto);

    TokenResponse verifyTwoFactorCode(TwoFactorLoginRequest request);

    void signup(SignUpDto sign);

    void changePassword(PasswordChangeRequest request);

    void enable2FA(Enable2FARequest request);

    TokenVerificationResponse verifyToken(String token);
}
