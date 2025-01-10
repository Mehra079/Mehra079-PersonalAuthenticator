package com.bootcamp.backend.controller;

import com.bootcamp.backend.dto.ErrorResponse;
import com.bootcamp.backend.dto.JwtResponse;
import com.bootcamp.backend.dto.LoginRequest;
import com.bootcamp.backend.dto.ResetPasswordRequest;
import com.bootcamp.backend.entity.User;
import com.bootcamp.backend.service.AuthService;
import com.bootcamp.backend.service.EmailService;
import com.bootcamp.backend.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {
        try {
            User loginUser = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            String jwt = jwtUtils.generateToken(loginRequest.getUsername(), loginRequest.getPassword());

            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (RuntimeException e) {
            // If login fails, return the exception message
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User createdUser = authService.register(user);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestParam String email) {
        authService.sendOtpEmail(email);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "OTP Verification successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOTP(@RequestBody Map<String, String> payload) {
        authService.verifyOtp(payload.get("email"), payload.get("otp"));
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "OTP Verification successful");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String newPassword = payload.get("newPassword");
        boolean passwordUpdated = authService.updatePassword(email, newPassword);
        Map<String, String> response = new HashMap<>();
        if(passwordUpdated){
            response.put("status", "success");
            response.put("message", "OTP Verification successful");

        }
        return ResponseEntity.ok(response);
    }
}

