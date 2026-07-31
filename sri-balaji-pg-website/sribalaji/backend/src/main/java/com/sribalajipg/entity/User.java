package com.sribalajipg.entity;

import jakarta.persistence.*;
import lombok.Data;

// Login identity for Admin / Manager / Tenant (role-based access)
@Entity
@Table(name = "users")
@Data
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String passwordHash;   // BCrypt encoded — never store plain text

    @Enumerated(EnumType.STRING)
    private Role role;

    private String fullName;
    private String email;
    private boolean active = true;
}
