package com.servicehub.servicehub_api.dto;

import com.servicehub.servicehub_api.model.Provider;

import java.time.LocalDateTime;

public record ProviderResponse(
        Long id,
        String name,
        String email,
        String phone,
        String bio,
        boolean active,
        LocalDateTime createdAt
) {
    public static ProviderResponse from(Provider provider) {
        return new ProviderResponse(
                provider.getId(),
                provider.getName(),
                provider.getEmail(),
                provider.getPhone(),
                provider.getBio(),
                provider.isActive(),
                provider.getCreatedAt()
        );
    }
}
