package com.servicehub.servicehub_api.dto;
import jakarta.validation.constraints.*;
public record ReviewRequest(@NotNull @Positive Long requestId, @NotNull @Min(1) @Max(5) Short rating, @Size(max = 2000) String comment) { }
