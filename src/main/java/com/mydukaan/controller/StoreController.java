package com.mydukaan.controller;

import com.mydukaan.dto.request.StoreRequest;
import com.mydukaan.dto.common.ApiResponse;
import com.mydukaan.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store")
public class StoreController {
    private final StoreService service;

    public StoreController(StoreService service) {
        this.service = service;
    }

    @PostMapping("/create-store")
    public ResponseEntity<ApiResponse> createStore(@RequestBody StoreRequest store) {
        return ResponseEntity.status(200).body(service.create(store, null));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> getStores(){
        return ResponseEntity.status(200).body(service.getStores());
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponse> getStore(@PathVariable Long storeId){
        return ResponseEntity.status(200).body(service.getStore(storeId));
    }

    @DeleteMapping("delete-store/{id}")
    public ResponseEntity<ApiResponse> deleteStore(@PathVariable Long id){
        return ResponseEntity.status(200).body(service.deleteStore(id));
    }
}
