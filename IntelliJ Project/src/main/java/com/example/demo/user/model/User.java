package com.example.demo.user.model;

import com.example.demo.user.roles.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
/**
 * Entity representing a user in the system.
 * Contains user details and implements UserDetails for Spring Security.
 */
@Entity
@Table(name = "_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    // Unique identifier for the user
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Name of the user
    private String name;

    // Email of the user, used as the username for authentication
    private String email;

    // Hashed password of the user
    private String password;

    // Role assigned to the user (e.g., ADMIN, USER)
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Constructor to initialize a user with name, email, password, and role.
     * @param name Name of the user.
     * @param email Email of the user.
     * @param password Password of the user.
     * @param role Role assigned to the user.
     */
    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Returns the authorities (roles) granted to the user.
     * @return A collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Returns the email as the username used for authentication.
     * @return Email of the user.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates whether the user's account has expired.
     * @return true if the account is valid (non-expired), false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * @return true if the user is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) are non-expired.
     * @return true if the credentials are valid (non-expired), false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
