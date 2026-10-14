package com.placement.spms.dto;

import com.placement.spms.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuthDtos {
    public record RegisterRequest(
            @NotBlank String fullName,
            @Email @NotBlank String email,
            @NotBlank String password,
            @NotNull Role role,
            String studentId,
            String department,
            Integer graduationYear,
            String companyName,
            String contactPerson,
            String contactNumber
    ) {}

    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}

    public record AuthResponse(String token, Long userId, String fullName, String email, Role role) {}
}
