package com.sau.swe.dto;

import lombok.Getter;

@Getter
public class PasswordChangeRequest {
    public Long userId;
    public String oldPassword;
    public String newPassword;
}
