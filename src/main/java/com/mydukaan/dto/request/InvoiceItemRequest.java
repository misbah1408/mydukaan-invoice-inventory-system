package com.mydukaan.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItemRequest {
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}