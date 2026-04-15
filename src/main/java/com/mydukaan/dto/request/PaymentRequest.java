package com.mydukaan.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentRequest {
    private Long invoiceId;
    private Long ledgerId;
    private BigDecimal amount;
    private String method;
    private String transactionId;
    private LocalDateTime createdAt;
}