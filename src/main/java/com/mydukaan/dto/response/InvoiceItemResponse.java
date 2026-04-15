package com.mydukaan.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InvoiceItemResponse {

    private Long id;
    private Long productId;
    private String productName;

    private Integer quantity;
    private BigDecimal price;

    private BigDecimal total;
}