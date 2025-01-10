package com.bootcamp.backend.dto;

public class ErrorResponse {
    private String message;
    private int remainingAttempts; // Optional field for remaining attempts

    public ErrorResponse(String message) {
        this.message = message;
    }

    // If needed, provide the number of remaining login attempts
    public ErrorResponse(String message, int remainingAttempts) {
        this.message = message;
        this.remainingAttempts = remainingAttempts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public void setRemainingAttempts(int remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }
}
