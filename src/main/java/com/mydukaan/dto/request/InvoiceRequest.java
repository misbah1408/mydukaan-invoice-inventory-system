package com.mydukaan.dto.request;

import com.mydukaan.enums.InvoiceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceRequest {

    private Long userId;
    private Long storeId;
    private Long customerId;
    private String invoiceNumber;
    private InvoiceStatus status;
    private BigDecimal gstRate;
    private BigDecimal discount;
    private LocalDateTime createdAt;
    private List<InvoiceItemRequest> items;
    private List<PaymentRequest> payments;
}