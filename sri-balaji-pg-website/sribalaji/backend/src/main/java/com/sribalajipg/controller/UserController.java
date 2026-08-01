package com.sribalajipg.controller;

import com.sribalajipg.dto.CreateUserRequest;
import com.sribalajipg.entity.Role;
import com.sribalajipg.entity.User;
import com.sribalajipg.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// Only an already-authenticated ADMIN can create new accounts (including new admins/managers/tenants).
// This is intentional: there is no public self-signup endpoint, so a stranger can't mint themselves
// an ADMIN account by hitting an API route directly.
@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {

        if (request.getMobileNumber() == null || request.getPassword() == null
                || request.getRole() == null) {
            return ResponseEntity.badRequest().body("mobileNumber, password and role are required");
        }

        if (request.getPassword().length() < 8) {
            return ResponseEntity.badRequest().body("Password must be at least 8 characters");
        }

        if (userRepository.findByMobileNumber(request.getMobileNumber()).isPresent()) {
            return ResponseEntity.status(409).body("An account with this mobile number already exists");
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("role must be one of ADMIN, MANAGER, TENANT");
        }

        User user = new User();
        user.setMobileNumber(request.getMobileNumber());
        // The raw password is hashed here and only the hash is ever persisted.
        // request.getPassword() is not stored, logged, or returned anywhere below this line.
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(role);
        user.setActive(true);

        userRepository.save(user);

        // Deliberately not returning the saved User object — it carries passwordHash.
        // Return only what the caller needs to confirm success.
        return ResponseEntity.ok().body("User created: " + user.getMobileNumber() + " (" + role + ")");
    }
}
