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

    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse> updateCustomer(
            @PathVariable Long customerId,
            @RequestBody CustomerRequest request) {
        return ResponseEntity.status(200).body(customerService.updateCustomer(customerId, request));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.status(200).body(
                new ApiResponse<>(true, "Success", "Customer deleted successfully")
        );
    }
}