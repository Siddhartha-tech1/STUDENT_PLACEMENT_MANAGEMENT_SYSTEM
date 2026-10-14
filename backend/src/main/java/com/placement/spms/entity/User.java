package com.placement.spms.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document("users")
public class User {
    @Id
    private String id;

    private String fullName;

    @Indexed(unique = true)
    private String email;

    private String passwordHash;

    private Role role;

    private AccountStatus status = AccountStatus.ACTIVE;

    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; touch(); }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; touch(); }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; touch(); }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; touch(); }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; touch(); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    private void touch() { updatedAt = Instant.now(); }
}
