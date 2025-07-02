package com.home.kanjidictionaryapp.util;

import com.home.kanjidictionaryapp.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtTokenUtil {

    private static final String SECRET = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327855";
    private final SecretKey key;

    public JwtTokenUtil() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", user.getUsername());
        payload.put("roles", user.getRole().getAuthority());
        payload.put("iat", new Date().getTime());
        payload.put("exp", new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10).getTime());

        return Jwts.builder()
                .claims(payload)
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("sub", String.class);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

}
