package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.controller.dto.AuthToken;
import com.example.TaskManagementSystem.controller.dto.UserDTO;
import com.example.TaskManagementSystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public AuthToken login (@RequestParam String login, @RequestParam String password) {
        return authService.login(login, password);
    }
    @PostMapping("/refresh")
    public String refresh(@RequestParam String refreshToken) {
        return authService.refreshToken(refreshToken);
    }
    @PostMapping("/register")
    public void register(@RequestBody UserDTO userDTO) {
        authService.registerUser(userDTO);
    }
}
