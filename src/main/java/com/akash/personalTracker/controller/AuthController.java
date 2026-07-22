package com.akash.personalTracker.controller;

import com.akash.personalTracker.dto.AuthResponse;
import com.akash.personalTracker.dto.LoginRequest;
import com.akash.personalTracker.dto.RegisterRequest;
import com.akash.personalTracker.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        System.out.println(">>> REGISTER ENDPOINT HIT WITH EMAIL: " + request.getEmail());
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}