package com.servicehub.servicehub_api.dto;
import com.servicehub.servicehub_api.model.ServiceRequestStatus;
import jakarta.validation.constraints.NotNull;
public record ServiceRequestStatusRequest(@NotNull ServiceRequestStatus status) { }
