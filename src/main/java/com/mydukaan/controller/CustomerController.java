package com.mydukaan.controller;

import com.mydukaan.dto.common.ApiResponse;
import com.mydukaan.dto.request.CustomerRequest;
import com.mydukaan.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createCustomer(@RequestBody CustomerRequest customer) {
        return ResponseEntity.status(201).body(customerService.createCustomer(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCustomer(@PathVariable Long id) {
        return ResponseEntity.status(200).body(customerService.getCustomerById(id));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse> getByStore(@PathVariable Long storeId) {
        return ResponseEntity.status(200).body(customerService.getCustomersByStore(storeId));
    }

    @PutMapping()
    public ResponseEntity<ApiResponse> updateCustomer(
            @RequestParam Long id,
            @RequestBody CustomerRequest request) {
        return ResponseEntity.status(200).body(customerService.updateCustomer(id, request));
    }

    @DeleteMapping()
    public ResponseEntity<ApiResponse> deleteCustomer(@RequestParam Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.status(200).body(
                new ApiResponse<>(true, "Success", "Customer deleted successfully")
        );
    }
}