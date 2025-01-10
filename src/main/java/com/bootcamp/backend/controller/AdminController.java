package com.bootcamp.backend.controller;

import com.bootcamp.backend.dto.UserResponseDTO;
import com.bootcamp.backend.entity.User;
import com.bootcamp.backend.exception.UserUpdateException;
import com.bootcamp.backend.service.UserService;
import com.bootcamp.backend.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<UserResponseDTO> getUsers() {
        List<User> users = userService.getUsers();
        return users.stream()
                .map(user -> new UserResponseDTO(user.getUsername(), user.getEmail(), user.isLocked(), user.getRole()))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String username) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.deleteUser(username);
            response.put("message", "User deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            response.put("error", "User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/lock/{username}")
    public ResponseEntity<UserResponseDTO> lockUser(@PathVariable String username) {
        try {
            // Lock the user account
            User lockedUser = userService.lockUser(username);

            // Return the updated user details as a response
            UserResponseDTO responseDTO = new UserResponseDTO(
                    lockedUser.getUsername(),
                    lockedUser.getEmail(),
                    lockedUser.isLocked(),
                    lockedUser.getRole()
            );

            // Return success with updated user data
            return ResponseEntity.ok(responseDTO);
        } catch (UserNotFoundException e) {
            // Return user not found error
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("/unlock/{username}")
    public ResponseEntity<UserResponseDTO> unlockUser(@PathVariable String username) {
        try {
            // Lock the user account
            User unlockedUser = userService.unlockUser(username);

            // Return the updated user details as a response
            UserResponseDTO responseDTO = new UserResponseDTO(
                    unlockedUser.getUsername(),
                    unlockedUser.getEmail(),
                    unlockedUser.isLocked(),
                    unlockedUser.getRole()
            );

            // Return success with updated user data
            return ResponseEntity.ok(responseDTO);
        } catch (UserNotFoundException e) {
            // Return user not found error
            return ResponseEntity.status(404).body(null);
        }
    }


    @PutMapping("/make-admin/{username}")
    public ResponseEntity<UserResponseDTO> makeAdmin(@PathVariable String username) {
        try {
            // Lock the user account
            User adminUser = userService.makeUserAdmin(username);

            // Return the updated user details as a response
            UserResponseDTO responseDTO = new UserResponseDTO(
                    adminUser.getUsername(),
                    adminUser.getEmail(),
                    adminUser.isLocked(),
                    adminUser.getRole()
            );

            // Return success with updated user data
            return ResponseEntity.ok(responseDTO);
        } catch (UserNotFoundException e) {
            // Return user not found error
            return ResponseEntity.status(404).body(null);
        }
        catch (UserUpdateException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

}
