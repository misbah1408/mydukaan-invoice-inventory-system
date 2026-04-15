package com.mydukaan.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedgerAdjRequest {
    private String type;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private boolean added;
}
