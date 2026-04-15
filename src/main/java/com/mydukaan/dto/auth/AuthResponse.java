package com.mydukaan.dto.auth;

import lombok.Getter;

@Getter
public class AuthResponse {

    private final String message;
    private final String token;

    public AuthResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }
}