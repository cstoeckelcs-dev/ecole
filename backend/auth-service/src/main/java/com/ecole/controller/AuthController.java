package com.ecole.controller;

import com.ecole.dto.LoginRequest;
import com.ecole.dto.RegisterRequest;
import com.ecole.dto.TokenResponse;
import com.ecole.dto.UserResponse;
import com.ecole.service.AuthService;
import com.ecole.service.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final GoogleAuthService googleAuthService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestParam String refreshToken) {
        TokenResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/google")
    public ResponseEntity<String> getGoogleAuthUrl() {
        String url = googleAuthService.getGoogleAuthUrl();
        return ResponseEntity.ok(url);
    }

    @GetMapping("/google/callback")
    public ResponseEntity<UserResponse> googleCallback(@RequestParam String code) {
        UserResponse response = googleAuthService.authenticateWithGoogle(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        // Implementation to get current user from security context
        return ResponseEntity.ok(null);
    }
}
