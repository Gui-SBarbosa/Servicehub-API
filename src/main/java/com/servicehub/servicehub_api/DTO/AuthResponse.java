package com.servicehub.servicehub_api.dto;
public record AuthResponse(String accessToken, String tokenType, long expiresInSeconds, UserResponse user) { }
