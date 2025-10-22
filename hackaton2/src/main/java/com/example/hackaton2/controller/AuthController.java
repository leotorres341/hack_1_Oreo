package com.example.hackaton2.controller;

import com.example.hackaton2.auth.AuthService;
import com.example.hackaton2.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User saved = authService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String token = authService.login(payload.get("username"), payload.get("password"));
        return ResponseEntity.ok(Map.of(
                "token", token,
                "expiresIn", 3600
        ));
    }
}
