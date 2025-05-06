package com.example.ClinicBooking.config;

import com.example.ClinicBooking.entity.User;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "ye4d1o8g4a6w1a9c2o@n#a$n@4c1o8n6a2n9";

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getPhoneNumber())
                .claim("role", mapRole(user.getRole()))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractPhone(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token) {
        return (String) Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    public boolean validateToken(String token, User user) {
        return extractPhone(token).equals(user.getPhoneNumber()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    public String mapRole(int role) {
        return switch (role) {
            case 0 -> "ROLE_ADMIN";
            case 1 -> "ROLE_PATIENT";
            case 2 -> "ROLE_STAFF";
            default -> "ROLE_UNKNOWN";
        };
    }
}

