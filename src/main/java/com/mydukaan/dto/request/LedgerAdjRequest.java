package com.mydukaan.dto.request;

import com.mydukaan.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedgerAdjRequest {
    private Long ledgerId;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
