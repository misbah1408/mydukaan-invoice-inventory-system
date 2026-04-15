package com.mydukaan.dto.auth;

import com.mydukaan.dto.request.StoreRequest;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RegisterWithStoreRequest {
    private RegisterRequest registerRequest;
    private StoreRequest storeRequest;
}
