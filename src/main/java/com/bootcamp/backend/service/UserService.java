package com.bootcamp.backend.service;

import com.bootcamp.backend.dto.UserDTO;
import com.bootcamp.backend.entity.User;
import com.bootcamp.backend.exception.UserNotFoundException;
import com.bootcamp.backend.exception.UserUpdateException;
import com.bootcamp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        return user;
    }

    public User updateProfile(String username, UserDTO updatedUser) {
        return userRepository.findByUsername(username).map(existingUser -> {
            try {
                // Update fields
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setPassword(updatedUser.getPassword());
                existingUser.setEmail(updatedUser.getEmail());

                // Save the updated user to the database
                User savedUser = userRepository.save(existingUser);
                return savedUser;
            } catch (Exception e) {
                throw new UserUpdateException("Failed to update profile for user " + username);
            }
        }).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + "not found"));
        userRepository.delete(user);
    }

    public User lockUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setLocked(true);
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User unlockUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setLocked(false);
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User updateUserProfile(String username, UserDTO updatedUser) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        try {
            // Update user fields
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());

            return userRepository.save(existingUser);
        } catch (Exception e) {
            throw new UserUpdateException("Failed to update user profile.");
        }
    }

    public List<User> getUsers() {
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            throw new UserNotFoundException("No users found in the database.");
        }
        return userList;
    }

    public User makeUserAdmin(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check if the user is already an admin
        if ("ROLE_ADMIN".equals(user.getRole())) {
            throw new UserUpdateException("User is already an admin.");
        }

        // Change the user's role to admin
        user.setRole("ROLE_ADMIN");
        User savedUser = userRepository.save(user);
        return savedUser;    }

}
