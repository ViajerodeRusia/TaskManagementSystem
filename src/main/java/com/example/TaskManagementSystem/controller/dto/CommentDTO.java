package com.example.TaskManagementSystem.controller.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private String text;
    private Long taskId;
    private Long userId;
}
