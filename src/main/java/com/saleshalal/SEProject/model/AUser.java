package com.saleshalal.SEProject.model;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@EqualsAndHashCode
@ToString
@Setter
public abstract class AUser {

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false)
    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    protected AUser() {
    }

    public AUser(String email, String password, String name, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public @Email(message = "Please provide a valid email address") @NotBlank(message = "Email is required") String getEmail() {
        return this.email;
    }

    public @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String getPassword() {
        return this.password;
    }

    public @NotBlank(message = "Name is required") String getName() {
        return this.name;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public UserRole getRole() {
        return this.role;
    }
}
