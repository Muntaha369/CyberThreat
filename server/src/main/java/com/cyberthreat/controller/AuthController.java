package com.cyberthreat.controller;

import com.cyberthreat.model.User;
import com.cyberthreat.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import java.util.Collections;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegistrationRequest request) {
        try {
            String token = authService.registerUser(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginRequest request) {
        try {
            String token = authService.loginUser(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (RuntimeException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/user")
public ResponseEntity<User> getUser(@RequestHeader("Authorization") String token) {
    try {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // String jwtToken = token.replace("Bearer ", "").trim();
        // if (jwtToken.isEmpty()) {
        //     return ResponseEntity.badRequest().build();
        // }
        
        User user = authService.getUserFromToken(token);
        return ResponseEntity.ok(user);
    } catch (JwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}

    // Inner DTO classes
    static class RegistrationRequest {
        private String email;
        private String password;

        // Getters and setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    static class LoginRequest {
        private String email;
        private String password;

        // Getters and setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}