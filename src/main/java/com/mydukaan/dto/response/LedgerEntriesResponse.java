package com.mydukaan.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class LedgerEntriesResponse {
    private Long id;
    private String name;
    private String type;
    private Long paymentId;
    private Long ledgerId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
