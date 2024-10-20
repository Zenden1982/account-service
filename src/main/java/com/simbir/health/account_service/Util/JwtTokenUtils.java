package com.simbir.health.account_service.Util;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.simbir.health.account_service.Class.IssuedToken;
import com.simbir.health.account_service.Class.TokenPair;
import com.simbir.health.account_service.Class.User;
import com.simbir.health.account_service.Repository.IssuedTokenRepository;
import com.simbir.health.account_service.Repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jwt")
@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    @Value("f9JgP5qXwXmZTQmQ5zKc8v8h6ZTQbYqP9wRpL5cU5sA=")
    private String secret;

    private Duration accessTokenDuration = Duration.ofSeconds(50);

    private Duration refreshTokenDuration = Duration.ofDays(1);

    private final IssuedTokenRepository issuedTokenRepository;

    private final UserRepository userRepository;

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

        UUID jti = java.util.UUID.randomUUID();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        IssuedToken issuedToken = new IssuedToken(jti.toString(), user, false);
        issuedTokenRepository.save(issuedToken);
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .id(jti.toString())
                .issuedAt(issueDate)
                .expiration(expirationDate)
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public TokenPair refresh(String refreshToken, UserDetails userDetails) {
        if (!Boolean.TRUE.equals(getClaimsFromToken(refreshToken).getExpiration().before(new Date()))) {
            String jti = getClaimsFromToken(refreshToken).getId();
            IssuedToken issuedToken = issuedTokenRepository.findById(jti).get();
            if (Boolean.FALSE.equals(issuedToken.getIsRevoked())) {
                return new TokenPair(generateRefreshToken(userDetails),
                        generateAccessToken(userDetails));
            } else {
                throw new RuntimeException("Refresh token has been revoked");
            }
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }

    public void signOut(String token) {
        List<IssuedToken> issuedTokens = issuedTokenRepository.findAllByUserUsername(getUsernameFromToken(token));
        issuedTokens.forEach(issuedToken -> issuedToken.setIsRevoked(true));
        issuedTokenRepository.saveAll(issuedTokens);
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
