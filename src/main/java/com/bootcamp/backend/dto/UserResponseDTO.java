package com.bootcamp.backend.dto;

public class UserResponseDTO {
    private String username;
    private String email;
    private boolean locked;   // Locked status of the user
    private String role;

    public UserResponseDTO() {}

    public UserResponseDTO(String username, String email, boolean locked, String role) {
        this.username = username;
        this.email = email;
        this.locked = locked;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
