package com.servicehub.servicehub_api.dto;
import com.servicehub.servicehub_api.model.Role;
import jakarta.validation.constraints.*;
public record RegisterRequest(
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 8, max = 72) String password,
        @NotNull Role role,
        @Size(max = 20) String phone,
        @Size(max = 2000) String bio,
        @Size(max = 500) @Pattern(regexp = "^(https?://.+)?$", message = "avatarUrl deve usar http ou https") String avatarUrl) { }
