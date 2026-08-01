package com.sribalajipg.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String mobileNumber;
    private String password;      // raw password from the form — gets hashed before saving, never stored as-is
    private String fullName;
    private String email;
    private String role;          // "ADMIN" | "MANAGER" | "TENANT"
}
