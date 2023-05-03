package com.gitoshh.rideshare.OnboardingService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.signing.key}")
    private String signingKey;

    /**
     * Extracts the username from a JWT token
     * @param jwtToken - JWT token
     * @return - username
     */
    public String getUsernameFromToken(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public boolean validateToken(String jwt, UserDetails userDetails) {
        final String username = getUsernameFromToken(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token
     * @param claims - claims to be added to the JWT token
     * @param userDetails - user details
     * @return - JWT token
     */
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts a claim from a JWT token
     * @param jwtToken - JWT token
     * @param claimsResolver - function that extracts a claim from a JWT token
     * @return - claim
     * @param <T> - type of the claim
     */
    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] signingKeyBytes = Decoders.BASE64.decode(signingKey);
        return Keys.hmacShaKeyFor(signingKeyBytes);
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }
}
