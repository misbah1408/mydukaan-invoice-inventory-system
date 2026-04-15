package com.mydukaan.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AuthData {
    private String token;

    public AuthData(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AuthData{" +
                "token='" + token + '\'' +
                '}';
    }
}