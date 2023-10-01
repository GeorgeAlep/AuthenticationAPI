package com.example.demo.auth.controller;

import com.example.demo.auth.AuthenticationReponse;
import com.example.demo.auth.AuthenticationRequest;
import com.example.demo.auth.service.AuthenticationService;
import com.example.demo.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling authentication-related requests.
 * Provides endpoints for user registration and authentication.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    // Service to handle authentication operations
    private final AuthenticationService authenticationService;

    /**
     * Endpoint to register a new user.
     * @param request Contains user registration details.
     * @return Authentication response with JWT token.
     */
    @PostMapping(path =  "/register")
    public ResponseEntity<AuthenticationReponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    /**
     * Endpoint to authenticate an existing user.
     * @param request Contains user authentication details.
     * @return Authentication response with JWT token.
     */
    @PostMapping(path = "/authenticate")
    public ResponseEntity<AuthenticationReponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
