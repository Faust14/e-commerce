package com.shop.user_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.security.Key;
import java.util.function.Function;

    @Component
    public class JwtUtil {
        private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymy"; // Min 32 karaktera
        private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 sat

        private Key getSigningKey() {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
        }

        public String generateToken(String email) {
            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        public boolean validateToken(String token, String email) {
            return (email.equals(extractEmail(token)) && !isTokenExpired(token));
        }

        public String extractEmail(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        public Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }

        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
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
