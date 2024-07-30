package com.example.hotsix.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String msg;

    public LoginResponse(String msg) {}
}
