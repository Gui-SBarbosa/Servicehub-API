package com.servicehub.servicehub_api.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
public record ServiceRequestCreateRequest(@NotNull @Positive Long serviceId, @FutureOrPresent LocalDateTime scheduledAt, @Size(max = 3000) String notes) { }
