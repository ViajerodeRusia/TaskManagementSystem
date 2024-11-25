package com.example.TaskManagementSystem.controller.dto;

import com.example.TaskManagementSystem.db.entity.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String login;
    private String password;
    private Role role;
}
