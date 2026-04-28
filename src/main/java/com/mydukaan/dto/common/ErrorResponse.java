package com.mydukaan.dto.common;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ErrorResponse {
    private boolean success;
    private String error;
}
