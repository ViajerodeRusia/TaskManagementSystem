package com.example.TaskManagementSystem.service;

import com.example.TaskManagementSystem.controller.dto.AuthToken;
import com.example.TaskManagementSystem.controller.dto.UserDTO;
import com.example.TaskManagementSystem.db.entity.User;
import com.example.TaskManagementSystem.db.repository.UserRepository;
import com.example.TaskManagementSystem.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider; // Внедряем JwtTokenProvider
    private final BCryptPasswordEncoder passwordEncoder;
    private final Map<String, String> refreshTokens = new HashMap<>();

    public AuthToken login(String login, String password) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Invalid login or password"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            String accessToken = tokenProvider.generateAccessToken(login, user.getRole());
            String refreshToken = tokenProvider.generateRefreshToken(login);
            refreshTokens.put(refreshToken, login);
            return new AuthToken(accessToken, refreshToken);
        }
        throw new RuntimeException("Invalid login or password");
    }

    public String refreshToken(String refreshToken) {
        String login = refreshTokens.get(refreshToken);
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Invalid login or password"));
        if (login != null) {
            return tokenProvider.generateAccessToken(login, user.getRole());
        }
        throw new RuntimeException("Invalid refresh token");
    }

    public void registerUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());
        userRepository.save(user);
    }
}
