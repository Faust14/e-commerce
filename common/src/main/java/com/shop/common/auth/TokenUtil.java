package com.shop.common.auth;

import com.shop.common.config.AuthClientConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class TokenUtil {

    private final AuthClientConfig authClientConfig;

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(authClientConfig.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).get("email", String.class);
    }

    public SimpleGrantedAuthority extractRoles(String token) {
        String role = extractClaims(token).get("role", String.class);
        return new SimpleGrantedAuthority(role);
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);

            if (claims.getExpiration().before(new Date())) {
                return false;
            }

            String issuer = claims.getIssuer();
            return authClientConfig.getSecretIssuer().equals(issuer);
        } catch (Exception e) {
            return false;
        }
    }
}
