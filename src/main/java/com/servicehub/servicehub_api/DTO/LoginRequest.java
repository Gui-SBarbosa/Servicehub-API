package com.servicehub.servicehub_api.dto;
import jakarta.validation.constraints.*;
public record LoginRequest(@NotBlank @Email @Size(max = 255) String email, @NotBlank @Size(max = 72) String password) { }
