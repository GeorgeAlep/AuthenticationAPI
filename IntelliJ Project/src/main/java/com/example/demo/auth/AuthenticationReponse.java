package com.example.demo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the response payload for successful authentication.
 * Contains the JWT token for the authenticated user.
 */
@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class AuthenticationReponse {
    private String token;
}
