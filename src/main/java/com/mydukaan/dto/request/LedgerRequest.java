package com.mydukaan.dto.request;

import com.mydukaan.enums.AccountType;
import com.mydukaan.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LedgerRequest {
    private String displayName;
    private Long storeId;
    private AccountType accountType;
    private BigDecimal balance;
    private TransactionType type;
}
