package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.controller.dto.AuthToken;
import com.example.TaskManagementSystem.controller.dto.UserDTO;
import com.example.TaskManagementSystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления аутентификацией.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Выполняет вход пользователя.
     *
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return токен аутентификации
     */
    @PostMapping("/login")
    public AuthToken login (@RequestParam String login, @RequestParam String password) {
        return authService.login(login, password);
    }

    /**
     * Обновляет токен доступа.
     *
     * @param refreshToken токен обновления
     * @return новый токен доступа
     */
    @PostMapping("/refresh")
    public String refresh(@RequestParam String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param userDTO объект передачи данных пользователя
     */
    @PostMapping("/register")
    public void register(@RequestBody UserDTO userDTO) {
        authService.registerUser(userDTO);
    }
}
