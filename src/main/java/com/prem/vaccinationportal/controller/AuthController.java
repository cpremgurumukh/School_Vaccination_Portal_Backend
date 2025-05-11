package com.prem.vaccinationportal.controller;

import com.prem.vaccinationportal.model.User;
import com.prem.vaccinationportal.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (userRepository.findByUsername(request.username) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        User user = new User();
        user.setUsername(request.username);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setRole("COORDINATOR");
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = userRepository.findByUsername(request.username);
        if (user == null || !passwordEncoder.matches(request.password, user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
        return ResponseEntity.ok(new AuthResponse(true));
    }
}

class AuthRequest {
    public String username;
    public String password;
}

class AuthResponse {
    public boolean success;
    public AuthResponse(boolean success) {
        this.success = success;
    }
}
