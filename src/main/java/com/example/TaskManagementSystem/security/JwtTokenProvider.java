package com.example.TaskManagementSystem.security;

import com.example.TaskManagementSystem.db.entity.Role;
import com.example.TaskManagementSystem.db.entity.User;
import com.example.TaskManagementSystem.db.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

/**
 * Провайдер JWT токенов для аутентификации и авторизации.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final long accessTokenValidity = 10 * 60 * 1000; // 10 минут
    private final long refreshTokenValidity = 30 * 60 * 1000; // 30 минут

    /**
     * Проверяет валидность JWT токена.
     *
     * @param token JWT токен
     * @return true, если токен валиден, иначе false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Извлекает аутентификацию из JWT токена.
     *
     * @param token JWT токен
     * @return объект аутентификации
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        String roleName = claims.get("role", String.class);
        Role role = Role.valueOf(roleName);
        log.info("Extracted role from token: {}", roleName);

        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UsernamePasswordAuthenticationToken(user, token,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name())));
    }

    /**
     * Генерирует JWT токен доступа.
     *
     * @param username имя пользователя
     * @param role роль пользователя
     * @return токен доступа
     */
    public String generateAccessToken(String username, Role role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenValidity);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Генерирует JWT токен обновления.
     *
     * @param username имя пользователя
     * @return токен обновления
     */
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenValidity);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
