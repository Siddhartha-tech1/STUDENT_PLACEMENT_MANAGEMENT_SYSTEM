package com.placement.spms.controller;

import com.placement.spms.dto.AuthDtos.AuthResponse;
import com.placement.spms.dto.AuthDtos.LoginRequest;
import com.placement.spms.dto.AuthDtos.RegisterRequest;
import com.placement.spms.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
