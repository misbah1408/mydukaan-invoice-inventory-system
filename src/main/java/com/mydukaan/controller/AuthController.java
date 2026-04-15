package com.mydukaan.controller;

import com.mydukaan.dto.auth.*;
import com.mydukaan.dto.common.ApiResponse;
import com.mydukaan.dto.response.UserResponse;
import com.mydukaan.service.AuthService;
import com.mydukaan.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final StoreService storeService;

    public AuthController(AuthService authService, StoreService storeService) {
        this.authService = authService;
        this.storeService = storeService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }
    @PostMapping("/register-with-store")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterWithStoreRequest request) {
        ApiResponse<UserResponse> response = authService.register(request.getRegisterRequest());
        UserResponse user = response.getData();
        storeService.create(request.getStoreRequest(), user.getEmail());
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.status(200).body(authService.login(request));
    }
}