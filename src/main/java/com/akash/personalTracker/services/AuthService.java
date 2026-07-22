package com.akash.personalTracker.services;

import com.akash.personalTracker.config.JwtUtils;
import com.akash.personalTracker.dto.AuthResponse;
import com.akash.personalTracker.dto.LoginRequest;
import com.akash.personalTracker.dto.RegisterRequest;
import com.akash.personalTracker.entity.User;
import com.akash.personalTracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponse register(RegisterRequest request) {
        // 1. Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // 2. Create new user and hash the password
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // BCrypt Hashing!

        // 3. Save user to database
        User savedUser = userRepository.save(user);

        // 4. Generate JWT token
        String token = jwtUtils.generateToken(savedUser.getEmail(), savedUser.getUserId());

        return new AuthResponse(token,"Bearer", savedUser.getUserId(), savedUser.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // 2. Verify hashed password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 3. Generate JWT token
        String token = jwtUtils.generateToken(user.getEmail(), user.getUserId());

        return new AuthResponse(token, "Bearer", user.getUserId(), user.getEmail());
    }
}