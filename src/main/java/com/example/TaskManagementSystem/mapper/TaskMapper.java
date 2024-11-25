package com.example.TaskManagementSystem.mapper;

import com.example.TaskManagementSystem.controller.dto.TaskDTO;
import com.example.TaskManagementSystem.db.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "assignee.id", target = "assigneeId")
    TaskDTO toDTO(Task task);
    @Mapping(source = "assigneeId", target = "assignee.id")
    Task toEntity(TaskDTO taskDTO);
    List<TaskDTO> toDTO(List<Task> tasks);
}
