package com.example.demo.auth.service;

import com.example.demo.auth.AuthenticationReponse;
import com.example.demo.auth.AuthenticationRequest;
import com.example.demo.auth.RegisterRequest;
import com.example.demo.security.service.JwtService;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.roles.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user registration and authentication.
 * Handles the logic for registering a new user and authenticating existing users.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    // Repository to interact with user data in the database
    private final UserRepository userRepository;
    // Encoder to hash user passwords
    private final PasswordEncoder passwordEncoder;
    // Service to handle JWT operations
    private final JwtService jwtService;
    // Manager to handle authentication operations
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user.
     * @param request Contains user registration details.
     * @return Authentication response with JWT token.
     */
    public AuthenticationReponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email is already in use.");
        }

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationReponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Authenticates an existing user.
     * @param request Contains user authentication details.
     * @return Authentication response with JWT token.
     */
    public AuthenticationReponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationReponse.builder()
                .token(jwtToken)
                .build();
    }
}
