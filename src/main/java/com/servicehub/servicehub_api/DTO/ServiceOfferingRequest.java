package com.servicehub.servicehub_api.dto;
import com.servicehub.servicehub_api.model.ServiceCategory;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record ServiceOfferingRequest(
        @NotBlank @Size(max = 150) String title,
        @Size(max = 5000) String description,
        @NotNull @DecimalMin(value = "0.01") @Digits(integer = 8, fraction = 2) BigDecimal price,
        @NotNull ServiceCategory category) { }
