package com.sribalajipg.controller;

import com.sribalajipg.dto.LoginRequest;
import com.sribalajipg.dto.LoginResponse;
import com.sribalajipg.entity.User;
import com.sribalajipg.repository.UserRepository;
import com.sribalajipg.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Shared login endpoint for Tenant / Manager / Admin — role comes back in the token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByMobileNumber(request.getMobileNumber())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body("Invalid mobile number or password");
        }
        if (!user.isActive()) {
            return ResponseEntity.status(403).body("Account is deactivated. Contact the admin.");
        }

        String token = jwtUtil.generateToken(user.getMobileNumber(), user.getRole().name());
        return ResponseEntity.ok(new LoginResponse(token, user.getRole().name(), user.getFullName()));
    }
}
