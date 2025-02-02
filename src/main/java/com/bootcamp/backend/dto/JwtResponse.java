package com.bootcamp.backend.dto;

public class JwtResponse {

    private String token;
    private String type = "Bearer";  // You can set a fixed type like "Bearer"

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
