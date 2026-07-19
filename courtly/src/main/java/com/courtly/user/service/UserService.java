package com.courtly.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.courtly.user.dto.UserRegistrationRequest;
import com.courtly.user.dto.UserRegistrationResponse;
import com.courtly.user.entity.User;
import com.courtly.user.exception.UserAlreadyExistsException;
import com.courtly.user.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserRegistrationResponse register(UserRegistrationRequest request) {
        validateRegistrationAvailability(request.username(), request.email());

        String hashedPassword = passwordEncoder.encode(request.password());

        User user = new User(request.username(), request.email(), hashedPassword);

        User savedUser = userRepository.save(user);

        return new UserRegistrationResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(),
        savedUser.getCredits(), savedUser.getRole(), savedUser.getCreatedAt());
    }

    public void validateRegistrationAvailability(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Username is already registered");
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email is already registered");
        }
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}