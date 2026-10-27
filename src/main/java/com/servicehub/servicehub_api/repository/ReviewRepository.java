package com.servicehub.servicehub_api.repository;
import com.servicehub.servicehub_api.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReviewRepository extends JpaRepository<Review, Long> { boolean existsByRequestId(Long requestId); }
