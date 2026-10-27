package com.servicehub.servicehub_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_requests")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ServiceRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "service_id", nullable = false) private ServiceOffering service;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "client_id", nullable = false) private UserAccount client;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30) private ServiceRequestStatus status = ServiceRequestStatus.PENDING;
    @Column(name = "scheduled_at") private LocalDateTime scheduledAt;
    @Column(columnDefinition = "TEXT") private String notes;
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2) private BigDecimal totalPrice;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    public ServiceRequest(ServiceOffering service, UserAccount client, LocalDateTime scheduledAt, String notes) { this.service = service; this.client = client; this.scheduledAt = scheduledAt; this.notes = notes; this.totalPrice = service.getPrice(); }
    public void changeStatus(ServiceRequestStatus status) { this.status = status; }
    @PrePersist void created() { LocalDateTime now = LocalDateTime.now(); createdAt = now; updatedAt = now; }
    @PreUpdate void updated() { updatedAt = LocalDateTime.now(); }
}
