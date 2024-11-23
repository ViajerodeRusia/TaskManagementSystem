package com.example.TaskManagementSystem.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken {
    private String accessToken; // токен доступа
    private String refreshToken; // токен обновления
}
