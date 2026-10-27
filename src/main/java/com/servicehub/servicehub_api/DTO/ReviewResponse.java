package com.servicehub.servicehub_api.dto;
import com.servicehub.servicehub_api.model.Review;
import java.time.LocalDateTime;
public record ReviewResponse(Long id, Long requestId, Long reviewerId, short rating, String comment, LocalDateTime createdAt) { public static ReviewResponse from(Review review) { return new ReviewResponse(review.getId(), review.getRequest().getId(), review.getReviewer().getId(), review.getRating(), review.getComment(), review.getCreatedAt()); } }
