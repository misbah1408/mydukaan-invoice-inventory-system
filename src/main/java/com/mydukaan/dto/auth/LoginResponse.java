package com.mydukaan.dto.auth;

import com.mydukaan.dto.response.UserResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoginResponse {
    UserResponse userResponse;
    AuthData authData;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "userResponse=" + userResponse +
                ", authData=" + authData +
                '}';
    }
}
