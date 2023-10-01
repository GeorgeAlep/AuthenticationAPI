package com.example.demo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the request payload for user registration.
 * Contains the user's name, email, and password.
 */
@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
}
