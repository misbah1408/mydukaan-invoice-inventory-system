package com.mydukaan.service;

import com.mydukaan.dto.common.ApiResponse;
import com.mydukaan.dto.request.CustomerRequest;
import com.mydukaan.dto.response.CustomerResponse;
import com.mydukaan.model.Customer;
import com.mydukaan.model.Store;
import com.mydukaan.repository.CustomerRepository;
import com.mydukaan.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;

    public CustomerService(CustomerRepository customerRepository, StoreRepository storeRepository) {
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
    }

    public ApiResponse<CustomerResponse> createCustomer(CustomerRequest request) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setBalance(request.getBalance());
        customer.setStore(store);

        Customer saved = customerRepository.save(customer);

        return new ApiResponse<>(true, "Customer created successfully!!!", mapToResponse(saved));
    }

    public ApiResponse<CustomerResponse> getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        return new ApiResponse<>(true, "Success", mapToResponse(customer));
    }

    public ApiResponse<List<CustomerResponse>> getCustomersByStore(Long storeId) {
        return new ApiResponse<>(true, "Customer fetched successfully!!!", customerRepository.findByStoreId(storeId)
                .stream()
                .map(this::mapToResponse)
                .toList());
    }

    public ApiResponse<CustomerResponse> updateCustomer(Long id, CustomerRequest request) {
        Customer existing = getCustomerEntity(id);
        if (request.getName() != null) {
            existing.setName(request.getName());
        }
        if (request.getPhone() != null) {
            existing.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            existing.setEmail(request.getEmail());
        }
        if (request.getAddress() != null) {
            existing.setAddress(request.getAddress());
        }

        if (request.getBalance() != null) {
            existing.setBalance(request.getBalance());
        }

        Customer updated = customerRepository.save(existing);
        return new ApiResponse<>(true, "Success", mapToResponse(updated));
    }


    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private Customer getCustomerEntity(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .balance(customer.getBalance())
                .storeId(customer.getStore().getId())
                .storeName(customer.getStore().getName()) // if exists
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}