package com.blogsphere.blogsphere.controller;

import com.blogsphere.blogsphere.dto.*;
import com.blogsphere.blogsphere.model.RefreshToken;
import com.blogsphere.blogsphere.model.User;
import com.blogsphere.blogsphere.security.JwtUtil;
import com.blogsphere.blogsphere.service.EmailService;
import com.blogsphere.blogsphere.service.RefreshTokenService;
import com.blogsphere.blogsphere.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserService userService, RefreshTokenService refreshTokenService, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userService.getUserByUsername(request.getUsername());
        String accessToken = jwtUtil.generateToken(request.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        emailService.sendLoginAlertEmail(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        String newAccessToken = jwtUtil.generateToken(refreshToken.getUser().getUsername());

        return new AuthResponse(newAccessToken, refreshToken.getToken());
    }
}