package com.servicehub.servicehub_api.dto;
import com.servicehub.servicehub_api.model.UserAccount;
import java.time.LocalDateTime;
public record UserResponse(Long id, String fullName, String email, String role, String phone, String bio, String avatarUrl, boolean active, LocalDateTime createdAt) {
    public static UserResponse from(UserAccount user) { return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole().name(), user.getPhone(), user.getBio(), user.getAvatarUrl(), user.isActive(), user.getCreatedAt()); }
}
