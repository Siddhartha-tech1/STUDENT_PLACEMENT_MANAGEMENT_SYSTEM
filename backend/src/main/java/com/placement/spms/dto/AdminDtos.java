package com.placement.spms.dto;

import com.placement.spms.entity.AccountStatus;
import com.placement.spms.entity.Role;

public class AdminDtos {
    public record UserResponse(String id, String fullName, String email, Role role, AccountStatus status) {}
}
