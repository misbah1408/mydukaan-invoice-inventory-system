package com.mydukaan.dto.response;

import com.mydukaan.enums.InvoiceStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class InvoiceResponse {

    private Long id;
    private String invoiceNumber;

    private Long storeId;
    private Long customerId;

    private String customerName;

    private BigDecimal totalAmount;
    private BigDecimal gstAmount;

    private BigDecimal paidAmount;
    private BigDecimal dueAmount;
    private BigDecimal subTotal;
    private InvoiceStatus status;

    private LocalDateTime createdAt;

    private List<InvoiceItemResponse> items;
    private List<PaymentResponse> payments;
}