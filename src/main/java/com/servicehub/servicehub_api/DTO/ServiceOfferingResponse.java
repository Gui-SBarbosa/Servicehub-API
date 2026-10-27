package com.servicehub.servicehub_api.dto;
import com.servicehub.servicehub_api.model.ServiceOffering;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record ServiceOfferingResponse(Long id, String title, String description, BigDecimal price, String category, Long providerId, String providerName, boolean active, LocalDateTime createdAt) {
    public static ServiceOfferingResponse from(ServiceOffering service) { return new ServiceOfferingResponse(service.getId(), service.getTitle(), service.getDescription(), service.getPrice(), service.getCategory().name(), service.getProvider().getId(), service.getProvider().getFullName(), service.isActive(), service.getCreatedAt()); }
}
