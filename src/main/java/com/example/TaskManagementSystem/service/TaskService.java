package com.example.TaskManagementSystem.service;

import com.example.TaskManagementSystem.controller.dto.CommentDTO;
import com.example.TaskManagementSystem.controller.dto.TaskDTO;
import com.example.TaskManagementSystem.controller.dto.UpdateTaskDTO;
import com.example.TaskManagementSystem.db.entity.Task;
import com.example.TaskManagementSystem.db.entity.TaskStatus;
import com.example.TaskManagementSystem.db.entity.User;
import com.example.TaskManagementSystem.db.repository.CommentRepository;
import com.example.TaskManagementSystem.db.repository.TaskRepository;
import com.example.TaskManagementSystem.db.repository.UserRepository;
import com.example.TaskManagementSystem.mapper.CommentMapper;
import com.example.TaskManagementSystem.mapper.TaskMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    public TaskDTO createTask(TaskDTO taskDTO) {
        log.info("Creating task");
        Task task = taskMapper.toEntity(taskDTO);

        // Извлечение текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        // Установка автора
        task.setAuthor(currentUser);

        taskRepository.save(task);
        log.info("Created");
        return taskDTO;
    }
    public UpdateTaskDTO editTask(UpdateTaskDTO updateTaskDTO) {
        log.info("Updating task");

        // Загрузка существующей задачи
        Task existingTask = taskRepository.findById(updateTaskDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        // Обновление полей задачи
        existingTask.setTitle(updateTaskDTO.getTitle());
        existingTask.setDescription(updateTaskDTO.getDescription());
        existingTask.setTaskPriority(updateTaskDTO.getTaskPriority());
        existingTask.setTaskStatus(updateTaskDTO.getTaskStatus());
        Optional<User> assignedUserOpt = userRepository.findById(updateTaskDTO.getAssigneeId());
        if(assignedUserOpt.isPresent()) {
            User assignedUser = assignedUserOpt.get();
            existingTask.setAssignee(assignedUser);
        } else if(assignedUserOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Сохранение обновленной задачи
        taskRepository.save(existingTask);
        log.info("Updated");
        return updateTaskDTO;
    }

    public HttpStatus deleteTask(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            log.warn("Task with ID {} not found", taskId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        log.info("Deleting task with ID {}", taskId);
        taskRepository.deleteById(taskId);
        log.info("Deleted task with ID {}", taskId);
        return HttpStatus.OK;
    }
    public List<TaskDTO> getAllTasks() {
        log.info("Getting all tasks");
        return taskMapper.toDTO(taskRepository.findAll());
    }

    public List<TaskDTO> getUserTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public HttpStatus updateTaskStatus(Long id, String status) {
        log.info("Starting transaction for updating task status");
        TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase().trim());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Optional<Task> taskOpt = taskRepository.findById(id);
        if(taskOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        } else {
            Task task = taskOpt.get();
            if (task.getAssignee() == null || !task.getAssignee().getId().equals(currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this task");
            }
            task.setTaskStatus(taskStatus);
            taskRepository.save(task);
            return HttpStatus.OK;
        }
    }

    public CommentDTO addComment(Long id, String comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Optional<Task> taskOpt = taskRepository.findById(id);
        if(taskOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        } else {
            Task task = taskOpt.get();
            if (task.getAssignee() == null || !task.getAssignee().getId().equals(currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this task");
            }
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setText(comment);
            commentDTO.setTaskId(id);
            commentDTO.setUserId(currentUser.getId());

            commentRepository.save(commentMapper.toEntity(commentDTO));
            return commentDTO;
        }
    }

    public CommentDTO addCommentUser(Long id, String comment) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if(taskOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText(comment);
        commentDTO.setTaskId(id);
        commentDTO.setUserId(currentUser.getId());

        commentRepository.save(commentMapper.toEntity(commentDTO));
        return commentDTO;
    }
}
