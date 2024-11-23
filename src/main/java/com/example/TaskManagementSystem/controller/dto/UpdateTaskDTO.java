package com.example.TaskManagementSystem.controller.dto;

import com.example.TaskManagementSystem.db.entity.TaskPriority;
import com.example.TaskManagementSystem.db.entity.TaskStatus;
import lombok.Data;

@Data
public class UpdateTaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskPriority taskPriority;
    private TaskStatus taskStatus;
    private Long assigneeId;
}
