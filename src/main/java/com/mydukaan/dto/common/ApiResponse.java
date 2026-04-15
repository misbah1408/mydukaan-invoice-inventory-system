package com.mydukaan.dto.common;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

}