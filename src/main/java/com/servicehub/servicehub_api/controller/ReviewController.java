package com.servicehub.servicehub_api.controller;
import com.servicehub.servicehub_api.dto.*;
import com.servicehub.servicehub_api.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/reviews")
public class ReviewController { private final ReviewService service; public ReviewController(ReviewService service) { this.service = service; } @PostMapping @ResponseStatus(HttpStatus.CREATED) public ReviewResponse create(@Valid @RequestBody ReviewRequest request) { return service.create(request); } }
