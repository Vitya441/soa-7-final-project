package org.example.authservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Getter
public class JwtService {

    @Value("${app.jwt.secretKey}")
    private String secretKey;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration; // Время жизни access токена

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Извлечение ID пользователя (кастомный claim "id")
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class)); // Извлекаем как String
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Генерирует JWT access токен для UserDetails, включая указанный userId.
     */
    // TODO:
    public String generateAccessToken(UserDetails userDetails, Long userId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", customUserDetails.getRole().name());

        return buildToken(claims, userDetails, jwtExpiration);
    }

    /**
     * Общий метод для генерации JWT токена с любыми дополнительными claims и заданным временем истечения.
     *
     * @param extraClaims Map, содержащая дополнительные claims, которые нужно включить в токен.
     * @param userDetails Объект UserDetails, содержащий основные данные пользователя (username, authorities).
     * @param expiration  Время жизни токена в миллисекундах.
     * @return Сгенерированный JWT токен.
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
