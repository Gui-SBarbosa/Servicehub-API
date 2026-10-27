package com.servicehub.servicehub_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "request_id", nullable = false, unique = true) private ServiceRequest request;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "reviewer_id", nullable = false) private UserAccount reviewer;
    @Column(nullable = false) private short rating;
    @Column(columnDefinition = "TEXT") private String comment;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    public Review(ServiceRequest request, UserAccount reviewer, short rating, String comment) { this.request = request; this.reviewer = reviewer; this.rating = rating; this.comment = comment; }
    @PrePersist void created() { createdAt = LocalDateTime.now(); }
}
