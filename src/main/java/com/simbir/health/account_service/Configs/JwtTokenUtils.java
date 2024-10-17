package com.simbir.health.account_service.Configs;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtTokenUtils {

    @Value("f9JgP5qXwXmZTQmQ5zKc8v8h6ZTQbYqP9wRpL5cU5sA=")
    private String secret;

    private Duration accessTokenDuration = Duration.ofMinutes(30);

    private Duration refreshTokenDuration = Duration.ofDays(2);

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream().map(role -> role.getAuthority())
                .collect(Collectors.toList());
        claims.put("roles", roles);
        Date issueDate = new Date();
        Date expirationDate = new Date(issueDate.getTime() + accessTokenDuration.toMillis());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(issueDate)
                .expiration(expirationDate)
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Date issueDate = new Date();
        Date expirationDate = new Date(issueDate.getTime() + refreshTokenDuration.toMillis());
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(issueDate)
                .expiration(expirationDate)
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public void signOut() {
        SecurityContextHolder.clearContext();
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(getSignInKey())
                    .build().parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        return getClaimsFromToken(token)
                .get("roles", List.class);
    }

}
