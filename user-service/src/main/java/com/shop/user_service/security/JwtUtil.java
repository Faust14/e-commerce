package com.shop.user_service.security;

import com.shop.user_service.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.security.Key;
import java.util.Map;
import java.util.function.Function;

    @Component
    public class JwtUtil {
        private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymymysecretkeymysecretkeymysecretkeymy";
        private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 sat

        private Key getSigningKey() {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
        }

        public String generateToken(User user) {
            Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(expirationDate)
                    .setClaims(generateClaims(user,expirationDate))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        public String extractEmail(String token) {
            return extractClaim(token, this::extractEmail);
        }

        public Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }

        public boolean validateToken(String token, String email) {
            return (email.equals(extractEmail(token)) && !isTokenExpired(token));
        }

        private String extractEmail(Claims claims) {
            return claims.get("email", String.class);
        }

        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }

        private Map<String, Object> generateClaims(User user, Date expDate) {
            return Map.of(
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "role", user.getRole(),
                    "exp", expDate
            );
        }
        private Claims extractAllClaims(String token) {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        private boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }
    }
