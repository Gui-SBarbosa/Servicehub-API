package com.servicehub.servicehub_api.dto;
import com.servicehub.servicehub_api.model.ServiceRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record ServiceRequestResponse(Long id, Long serviceId, String serviceTitle, Long clientId, String status, LocalDateTime scheduledAt, String notes, BigDecimal totalPrice, LocalDateTime createdAt) {
    public static ServiceRequestResponse from(ServiceRequest request) { return new ServiceRequestResponse(request.getId(), request.getService().getId(), request.getService().getTitle(), request.getClient().getId(), request.getStatus().name(), request.getScheduledAt(), request.getNotes(), request.getTotalPrice(), request.getCreatedAt()); }
}
