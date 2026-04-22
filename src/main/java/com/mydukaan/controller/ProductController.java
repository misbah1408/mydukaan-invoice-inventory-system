package com.mydukaan.controller;


import com.mydukaan.dto.common.ApiResponse;
import com.mydukaan.dto.request.ProductRequest;
import com.mydukaan.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping("/product/create")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductRequest product){
        return ResponseEntity.status(201).body(service.addProduct(product));
    }

    @PostMapping("/product/create-multiple/{storeId}")
    public ResponseEntity<ApiResponse> createMultipleProduct(@PathVariable Long storeId, @RequestBody List<ProductRequest> products){
        return ResponseEntity.status(201).body(service.addMultipleProducts(products, storeId));
    }

    @GetMapping("/products/{storeId}")
    public ResponseEntity<ApiResponse> getProducts(@PathVariable Long storeId){
        return ResponseEntity.status(200).body(service.getProducts(storeId));
    }

    @PutMapping("/product/update/{productId}")
    public ResponseEntity<ApiResponse> updateProductProduct(@PathVariable Long productId, @RequestBody ProductRequest product){
        return ResponseEntity.status(201).body(service.updateProduct(product, productId));
    }

    @DeleteMapping("/product/delete/{storeId}/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long storeId, @PathVariable Long productId){
        return ResponseEntity.status(201).body(service.deleteProduct(storeId, productId));
    }

    @GetMapping("/search/{storeId}/{keyword}")
    public ResponseEntity searchProduct(@PathVariable Long storeId, @PathVariable String keyword){
        return ResponseEntity.status(200).body(service.searchProduct(storeId, keyword));
    }
}
