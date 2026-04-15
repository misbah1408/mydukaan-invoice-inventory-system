package com.mydukaan.dto.response;

import com.mydukaan.enums.AccountType;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class LedgerResponse {
    private Long ledgerId;
    private String displayName;
    private Long storeId;
    private AccountType accountType;
    private BigDecimal balance;
    private List<PaymentResponse> transactions;
}
