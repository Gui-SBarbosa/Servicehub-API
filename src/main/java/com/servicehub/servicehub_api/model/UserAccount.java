package com.servicehub.servicehub_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;
    @Column(nullable = false, unique = true, length = 255)
    private String email;
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private Role role;
    @Column(length = 20) private String phone;
    @Column(columnDefinition = "TEXT") private String bio;
    @Column(name = "avatar_url", length = 500) private String avatarUrl;
    @Column(name = "is_active", nullable = false) private boolean active = true;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;

    public UserAccount(String fullName, String email, String passwordHash, Role role, String phone, String bio, String avatarUrl) {
        this.fullName = fullName; this.email = email; this.passwordHash = passwordHash; this.role = role;
        this.phone = phone; this.bio = bio; this.avatarUrl = avatarUrl;
    }
    public void deactivate() { this.active = false; }
    @PrePersist void created() { LocalDateTime now = LocalDateTime.now(); createdAt = now; updatedAt = now; }
    @PreUpdate void updated() { updatedAt = LocalDateTime.now(); }
}
