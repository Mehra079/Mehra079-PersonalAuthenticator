package com.bootcamp.backend.service;

import com.bootcamp.backend.entity.User;
import com.bootcamp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private static final int MAX_FAILED_ATTEMPTS = 3;
//
//    private User toUser(UserDTO userDTO){
//        User user = null;
//        user.setUsername(userDTO.getUsername());
//        user.setEmail(userDTO.getEmail());
//        user.setPassword(PasswordEncoderUtil.hashPassword(userDTO.getPassword()));
//        return  user;
//    }
//
//    private UserDTO toUserDTO(User user){
//        UserDTO userDTO = null;
//        userDTO.setUsername(user.getUsername());
//        userDTO.setEmail(user.getEmail());
//        userDTO.setPassword(PasswordEncoderUtil.hashPassword(user.getPassword()));
//        return  userDTO;
//    }

    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }
        user.setPassword(user.getPassword());
        user.setRole("ROLE_USER");
        user.setLocked(false); // New user is not locked
        user.setFailedLoginAttempts(0); // Reset failed login attempts
        User savedUser = userRepository.save(user);

        sendOtpEmailForVerification(user);

        return savedUser;
    }

    public void sendOtpEmailForVerification(User user) {
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10)); // OTP valid for 10 minutes
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);
    }

    public String generateOtp() {
        return String.valueOf((int) (Math.random() * 9000) + 1000); // Generate a 4-digit OTP
    }

    public boolean verifyOtp(String email, String otp){
        User user = getUserByEmail(email);

        if (!otp.equals(user.getOtp()) || LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            throw new RuntimeException("Invalid or expired OTP");
        }
        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
        return true;
    }
    public boolean updatePassword(String email, String newPassword) {
        User user = getUserByEmail(email);
        if(!user.getPassword().equals(newPassword)){
            user.setPassword(newPassword);
            clearOtpData(user); // Clear OTP and expiry data
            userRepository.save(user);
            return true;
        }
        else{
            return false;
        }
    }

    public User login(String username, String password) {
        User user = getUserByUsername(username);

        if (user.isLocked()) {
            throw new RuntimeException("Your account is locked. Please contact the admin for support.");
        }

        boolean isPasswordValid = password.equals(user.getPassword());
        if (!isPasswordValid) {
            handleFailedLoginAttempt(user);
            throw new RuntimeException("Invalid credentials.");
        }
        resetFailedLoginAttempts(user);
        return user;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email not found"));
    }

    private void handleFailedLoginAttempt(User user) {
        user.setFailedLoginAttempts(user.getFailedLoginAttempts()+1);

        if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
            lockUserAccount(user);
            throw new RuntimeException("Account locked due to too many failed login attempts. Please contact admin.");
        }
        userRepository.save(user);

        int remainingAttempts = MAX_FAILED_ATTEMPTS - user.getFailedLoginAttempts();
        throw new RuntimeException("Invalid credentials. " + remainingAttempts + " attempts left.");
    }

    private void lockUserAccount(User user) {
        user.setLocked(true); // Lock the account after MAX_FAILED_ATTEMPTS
        userRepository.save(user);
    }

    private void resetFailedLoginAttempts(User user) {
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }

    private void clearOtpData(User user) {
        user.setOtp(null);
        user.setOtpExpiry(null);
    }

    public void sendOtpEmail(String email) {
        String otp = generateOtp();
        User user = getUserByEmail(email);
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10)); // OTP valid for 10 minutes
        userRepository.save(user);
        emailService.sendOtpEmail(user.getEmail(), otp);
    }
}
