package com.example.demo.configuration;

import com.example.demo.security.service.JwtService;
import com.example.demo.student.model.Student;
import com.example.demo.student.repository.StudentRepository;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.roles.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
/**
 * Configuration class for initializing the database.
 * Sets up initial data for the application on startup.
 */
@Configuration
@RequiredArgsConstructor
public class DatabaseConfiguration {

    // Service to handle JWT operations
    private final JwtService jwtService;
    // Service to load user details for authentication
    private final UserDetailsService userDetailsService;
    // Repository to interact with user data in the database
    private final UserRepository userRepository;
    // Repository to interact with student data in the database
    private final StudentRepository studentRepository;
    // Encoder to hash user passwords
    private final PasswordEncoder passwordEncoder;

    public static User admin = null;

    /**
     * Initializes the database with default data on application startup.
     * @return CommandLineRunner to execute the initialization.
     */
    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {

            if (userRepository.findByEmail("admin@gmail.com").isEmpty() && admin == null) {
                admin = new User(
                        "admin",
                        "admin@gmail.com",
                        passwordEncoder.encode("admin"),
                        Role.ROLE_ADMIN
                );
                userRepository.save(admin);

                // Generate JWT token for admin
                UserDetails adminDetails = userDetailsService.loadUserByUsername(admin.getUsername());
                String jwtToken = jwtService.generateToken(adminDetails);

                // Print the JWT token to the console (or save it wherever preferable)
                System.out.println("Admin JWT Token: " + jwtToken);
            }

            Student malcolm = new Student("Malcolm Duster");
            Student alex = new Student("Alex Jackson");
            studentRepository.saveAll(List.of(malcolm, alex));
        };
    }
}
