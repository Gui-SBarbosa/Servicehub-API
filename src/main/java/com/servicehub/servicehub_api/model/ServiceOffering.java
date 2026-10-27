package com.servicehub.servicehub_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "services")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ServiceOffering {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 150) private String title;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal price;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 50) private ServiceCategory category;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "provider_id", nullable = false) private UserAccount provider;
    @Column(name = "is_active", nullable = false) private boolean active = true;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    public ServiceOffering(String title, String description, BigDecimal price, ServiceCategory category, UserAccount provider) { update(title, description, price, category); this.provider = provider; }
    public void update(String title, String description, BigDecimal price, ServiceCategory category) { this.title = title; this.description = description; this.price = price; this.category = category; }
    public void deactivate() { active = false; }
    @PrePersist void created() { LocalDateTime now = LocalDateTime.now(); createdAt = now; updatedAt = now; }
    @PreUpdate void updated() { updatedAt = LocalDateTime.now(); }
}
