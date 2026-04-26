package com.nearbuy.backend.controller;

import com.nearbuy.backend.dto.auth.AuthResponse;
import com.nearbuy.backend.dto.auth.LoginRequest;
import com.nearbuy.backend.dto.auth.RegisterRequest;
import com.nearbuy.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and JWT login APIs")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public String register(@Valid @RequestBody RegisterRequest request){
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Login and generate JWT token")
    public AuthResponse login(@Valid @RequestBody LoginRequest request){
        return userService.login(request);
    }
}
