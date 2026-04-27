package com.mydukaan.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class StoreResponse {
    private Long id;
    private String userName;
    private String name;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "StoreResDto{" +
                "userName=" + userName +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
