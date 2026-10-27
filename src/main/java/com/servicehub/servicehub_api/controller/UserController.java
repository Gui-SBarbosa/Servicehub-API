package com.servicehub.servicehub_api.controller;
import com.servicehub.servicehub_api.dto.UserResponse;
import com.servicehub.servicehub_api.service.CurrentUserService;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/users")
public class UserController { private final CurrentUserService current; public UserController(CurrentUserService current) { this.current = current; } @GetMapping("/me") public UserResponse me() { return UserResponse.from(current.get()); } }
