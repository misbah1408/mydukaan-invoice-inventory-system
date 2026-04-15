package com.mydukaan.dto.request;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductRequest {
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private Integer threshold;
    private Long storeId;
}
