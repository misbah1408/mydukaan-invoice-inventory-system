package com.mydukaan.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerRequest {
    private String name;
    private String phone;
    private String email;
    private String address;
    private Long storeId;
    private BigDecimal balance;
}
