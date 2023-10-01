package com.example.demo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the request payload for user authentication.
 * Contains the user's email and password.
 */
@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class AuthenticationRequest {

    private String email;
    private String password;
}
