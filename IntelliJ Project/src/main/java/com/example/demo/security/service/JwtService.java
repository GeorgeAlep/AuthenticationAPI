package com.example.demo.security.service;

import com.example.demo.configuration.DatabaseConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * Service class for handling JWT-related operations.
 * This class provides methods for generating JWT tokens, validating tokens, extracting claims, and more.
 */
@Service
public class JwtService {

    // Secret key for signing and verifying JWT tokens
    private static final String SECRET_KEY = "E1FUGDN7CoxpR3/jtqwhBMED4nmFHiijlNLIpTPc0LrFOvih4w0X/UqyNcaKb4BK";

    /**
     * Generates a JWT token for the given user details.
     * The token contains the user's roles as claims.
     * @param userDetails UserDetails instance containing user information.
     * @return Generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(
                "roles",
                userDetails
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );

        return generateToken(claims, userDetails);
    }

    /**
     * Validates the given JWT token against the provided user details.
     * Checks if the token's username matches the user details and if the token has not expired.
     * @param token JWT token.
     * @param userDetails UserDetails instance.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the provided JWT token has expired.
     * @param token JWT token.
     * @return true if the token has expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     * @param token JWT token.
     * @return Date representing the expiration time of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts the username (in this case, email) from the JWT token.
     * @param jwt JWT token.
     * @return Username extracted from the token.
     */
    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    /**
     * Generates a JWT token with the provided claims and user details.
     * @param extraClaims Additional claims to include in the token.
     * @param userDetails UserDetails instance.
     * @return Generated JWT token.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        JwtBuilder jwtBuilder = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256);

        if (!userDetails.getUsername().equals(DatabaseConfiguration.admin.getUsername())) {
            jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + 10000 * 60));
        }

        return jwtBuilder.compact();
    }

    /**
     * Extracts a specific claim from the JWT token.
     * @param jwt JWT token.
     * @param claimsResolver Function to extract the desired claim.
     * @param <T> Type of the claim.
     * @return Extracted claim.
     */
    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     * @param jwt JWT token.
     * @return Claims instance containing all claims from the token.
     */
    private Claims extractAllClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * Retrieves the signing key used for JWT token generation and validation.
     * @return Key used for signing the JWT.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
