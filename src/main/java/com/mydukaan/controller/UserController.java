package com.mydukaan.controller;

import com.mydukaan.dto.request.UserRequest;
import com.mydukaan.dto.common.ApiResponse;
import com.mydukaan.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getUser(Authentication authentication) {
        return ResponseEntity.status(200).body(userService.getUser(authentication.getName()));
    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody  UserRequest userRequest, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUser(userId, userRequest));
    }
}
