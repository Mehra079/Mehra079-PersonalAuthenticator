package com.bootcamp.backend.controller;

import com.bootcamp.backend.dto.UserDTO;
import com.bootcamp.backend.dto.UserResponseDTO;
import com.bootcamp.backend.entity.User;
import com.bootcamp.backend.service.UserService;
import com.bootcamp.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/userprofile")
    public ResponseEntity<?> getUserProfile(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        if (username == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        User user = userService.getUserByUsername(username);
        String password = user.getPassword();
        user.setPassword(password);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        return ResponseEntity.ok(user);
    }

    @PutMapping("/updateprofile")
    public ResponseEntity<?> updateUserProfile(HttpServletRequest request, @RequestBody UserDTO updatedUser) {
        // Retrieve the username from the request attribute
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        if (username == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // Update user profile
        User updatedProfile = userService.updateProfile(username, updatedUser);
        if (updatedProfile == null) {
            return ResponseEntity.status(400).body("Unable to update user profile");
        }

        return ResponseEntity.ok(updatedProfile);
    }
}
