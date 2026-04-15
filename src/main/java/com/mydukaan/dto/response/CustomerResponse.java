package com.mydukaan.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CustomerResponse {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;

    private BigDecimal balance;
    private Long storeId;
    private String storeName; // optional but useful

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}