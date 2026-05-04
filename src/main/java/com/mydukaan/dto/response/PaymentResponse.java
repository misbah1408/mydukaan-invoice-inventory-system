package com.mydukaan.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {

    private Long id;
    private String name;
    private Long invoiceId;
    private String invoiceNumber;

    private Long ledgerId;
    private String ledgerName;

    private BigDecimal amount;
    private String method;
    private String status;

    private String type;
    private String transactionId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}