package org.api.config.jwtsecurity;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class JwtConfig {
    private static final long EXPIRATION_TIME = 3600000; // 1 hours
    private static final long REFRESH_EXPIRATION_TIME = 28800000;  // 8 hours

    @Value("${jwt.secret-key}")
    private String secretKey;

    public Long getUserIdFromJwtToken(String token) { //토큰 복호화
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token) {
        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return true;
    }

    public String generateAccessToken(Long userId) {
        return generateToken(userId, EXPIRATION_TIME);
    }

    public String generateRefreshToken(Long userId) {
        return generateToken(userId, REFRESH_EXPIRATION_TIME);
    }

    public String generateToken(Long userId, long duration) {
        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + duration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
