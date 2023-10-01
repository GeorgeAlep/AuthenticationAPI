package com.example.demo.security.authfilter;

import com.example.demo.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter responsible for JWT-based authentication.
 * This filter intercepts each incoming request, extracts the JWT token from the "Authorization" header,
 * validates the token, and sets the authentication in the security context.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    // Service to handle JWT-related operations
    private final JwtService jwtService;

    // Service to load user details based on the username (in this case, email)
    private final UserDetailsService userDetailsService;

    /**
     * Intercepts each request, extracts and validates the JWT token from the "Authorization" header,
     * and sets the authentication in the security context if the token is valid.
     *
     * @param request     The incoming HTTP request.
     * @param response    The HTTP response that will be sent back.
     * @param filterChain Represents the chain of filters that the request will go through.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Extract the "Authorization" header from the incoming request
        final String authHeader = request.getHeader("Authorization");

        // If the "Authorization" header is missing or doesn't start with "Bearer ", continue the filter chain without JWT validation
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token by removing the "Bearer " prefix
        final String jwt = authHeader.substring(7);

        // Retrieve the username (in this case, email) associated with the token
        final String userEmail = jwtService.extractUsername(jwt);

        // Check if the username is not null and if there's no existing authentication in the current security context
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Retrieve the user details associated with the extracted username
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            // Validate the JWT token to ensure it's not expired and matches the user details
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Create an authentication token and set it in the security context, effectively authenticating the user for the current request
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue with the rest of the filter chain, allowing subsequent filters to process the request
        filterChain.doFilter(request, response);
    }
}
