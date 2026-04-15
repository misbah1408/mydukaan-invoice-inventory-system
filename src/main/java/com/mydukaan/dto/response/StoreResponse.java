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
    private UserResponse user;
    private String name;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "StoreResDto{" +
                "user=" + user +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
