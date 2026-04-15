package com.mydukaan.controller;

import com.mydukaan.dto.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController()
public class HealthCheck {
    @GetMapping("/api/health")
    public ResponseEntity<ApiResponse<List<String>>> getHealth(){
        System.out.println("Hello, World!");
        List<String> data = List.of("Misbah", "Anwer", "Naseem", "Missriya");
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Hello, World!!", data));
    }
}
