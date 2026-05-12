package com.rasim.dax.service;

import com.rasim.dax.entity.User;
import com.rasim.dax.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("USERNAME ALREADY EXISTS");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");  // Default rol USER

        // YENİ FIELD-LƏRİ SAVE ET
        user.setFullName(user.getFullName());
        user.setEmail(user.getEmail());
        user.setDepartment(user.getDepartment());

        User savedUser = userRepository.save(user);

        return jwtService.generateToken(savedUser.getUsername(), savedUser.getRole(), savedUser.getId());
    }

    public String login(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("WRONG PASSWORD");
        }

        return jwtService.generateToken(existingUser.getUsername(), existingUser.getRole(), existingUser.getId());
    }
}