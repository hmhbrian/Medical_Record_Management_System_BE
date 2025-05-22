package com.example.ClinicBooking.config;

import com.example.ClinicBooking.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
    //private final String SECRET_KEY = "e4d1o8g4a6w1a9c2ohn8ahn6h4c1o8n6a2n9";
    //@Value("${jwt.secret}")
    //private String SECRET_KEY;
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String extractPhone(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractPhone(token).equals(userDetails.getUsername()) && !isExpired(token);
    }

    private boolean isExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }


    // Hàm để tạo token
    public String generateToken(User user,  String position) {
        return Jwts.builder()
                .setSubject(user.getPhoneNumber())
                .claim("role", mapRole(user.getRole()))
                .claim("position", position != null ? position : "")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .signWith(SECRET_KEY)
                .compact();
    }
    public String mapRole(int role) {
        return switch (role) {
            case 0 -> "ROLE_ADMIN";
            case 1 -> "ROLE_PATIENT";
            case 2 -> "ROLE_STAFF";
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }
}


