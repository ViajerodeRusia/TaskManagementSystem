package com.example.TaskManagementSystem.mapper;

import com.example.TaskManagementSystem.controller.dto.CommentDTO;
import com.example.TaskManagementSystem.db.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "user.id", target = "userId")
    CommentDTO toDTO(Comment comment);
    @Mapping(source = "taskId", target = "task.id")
    @Mapping(source = "userId", target = "user.id")
    Comment toEntity(CommentDTO commentDTO);
}
