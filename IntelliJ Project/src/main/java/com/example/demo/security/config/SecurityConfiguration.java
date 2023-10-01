package com.example.demo.security.config;

import com.example.demo.security.authfilter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * Configuration class for Spring Security.
 * This class sets up the security filter chain, configures authentication and authorization rules, and integrates JWT authentication.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    // Filter for JWT-based authentication
    private final JwtAuthFilter jwtAuthFilter;

    // Authentication provider for user authentication
    private final AuthenticationProvider authenticationProvider;

    /**
     * Configures the security filter chain with custom authentication and authorization rules.
     * @param http HttpSecurity instance.
     * @return SecurityFilterChain instance.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/api/v1/auth/**")
                                .permitAll()
                                .requestMatchers("/api/v1/students/*").hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated()
                ).sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
