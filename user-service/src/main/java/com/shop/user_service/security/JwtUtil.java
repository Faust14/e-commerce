package com.shop.user_service.security;

import com.shop.user_service.config.SecretKeyConfig;
import com.shop.user_service.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.security.Key;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    private final SecretKeyConfig secretKeyConfig;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyConfig.getSecretKey()));
    }

    public String generateToken(User user) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .setClaims(generateClaims(user, expirationDate))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKeyConfig.getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (claims.getExpiration().before(new Date())) {
                return false;
            }

            String issuer = claims.getIssuer();
            return secretKeyConfig.getSecretIssuer().equals(issuer);
        } catch (Exception e) {
            return false;
        }
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private Map<String, Object> generateClaims(User user, Date expDate) {
        return Map.of(
                "username", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole(),
                "exp", expDate,
                "iss", secretKeyConfig.getSecretIssuer()
        );
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private String extractEmail(Claims claims) {
        return claims.get("email", String.class);
    }

    String extractEmail(String token) {
        return extractClaim(token, this::extractEmail);
    }
}
