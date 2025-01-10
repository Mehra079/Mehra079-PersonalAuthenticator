package com.bootcamp.backend.dto;

public class ResetPasswordRequest {
    private String email;
    private String newPassword; // Add this field

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
