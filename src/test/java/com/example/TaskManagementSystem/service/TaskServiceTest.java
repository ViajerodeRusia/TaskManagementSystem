package com.example.TaskManagementSystem.service;

import com.example.TaskManagementSystem.controller.dto.CommentDTO;
import com.example.TaskManagementSystem.controller.dto.TaskDTO;
import com.example.TaskManagementSystem.controller.dto.UpdateTaskDTO;
import com.example.TaskManagementSystem.db.entity.Comment;
import com.example.TaskManagementSystem.db.entity.Task;
import com.example.TaskManagementSystem.db.entity.TaskStatus;
import com.example.TaskManagementSystem.db.entity.User;
import com.example.TaskManagementSystem.db.repository.CommentRepository;
import com.example.TaskManagementSystem.db.repository.TaskRepository;
import com.example.TaskManagementSystem.db.repository.UserRepository;
import com.example.TaskManagementSystem.mapper.CommentMapper;
import com.example.TaskManagementSystem.mapper.TaskMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void createTask_ShouldCreateTask() {
        // Arrange
        TaskDTO taskDTO = new TaskDTO();
        Task task = new Task();
        User user = new User();
        when(authentication.getPrincipal()).thenReturn(user);
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);

        // Act
        TaskDTO result = taskService.createTask(taskDTO);

        // Assert
        verify(taskRepository, times(1)).save(task);
        assertEquals(taskDTO, result);
    }

    @Test
    void editTask_ShouldEditTask_WhenTaskExists() {
        // Arrange
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO();
        updateTaskDTO.setId(1L);
        Task task = new Task();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        // Act
        UpdateTaskDTO result = taskService.editTask(updateTaskDTO);

        // Assert
        verify(taskRepository, times(1)).save(task);
        assertEquals(updateTaskDTO, result);
    }

    @Test
    void deleteTask_ShouldDeleteTask_WhenTaskExists() {
        // Arrange
        Task task = new Task();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        HttpStatus result = taskService.deleteTask(1L);

        // Assert
        verify(taskRepository, times(1)).deleteById(1L);
        assertEquals(HttpStatus.OK, result);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        // Arrange
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        taskService.getAllTasks();

        // Assert
        verify(taskMapper, times(1)).toDTO(Collections.emptyList());
    }

    @Test
    void updateTaskStatus_ShouldUpdateStatus_WhenTaskExists() {
        // Arrange
        Task task = new Task();
        User user = new User();
        user.setId(1L);
        task.setAssignee(user);
        when(authentication.getPrincipal()).thenReturn(user);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        HttpStatus result = taskService.updateTaskStatus(1L, "COMPLETED");

        // Assert
        verify(taskRepository, times(1)).save(task);
        assertEquals(HttpStatus.OK, result);
    }

    @Test
    void addComment_ShouldAddComment_WhenTaskExists() {
        // Arrange
        Long taskId = 1L;
        String commentText = "Test comment";
        User user = new User();
        user.setId(1L);
        Task task = new Task();
        task.setId(taskId);
        task.setAssignee(user);
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText(commentText);
        commentDTO.setTaskId(taskId);
        commentDTO.setUserId(user.getId());

        when(authentication.getPrincipal()).thenReturn(user);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(commentMapper.toEntity(any(CommentDTO.class))).thenReturn(new Comment());

        // Act
        CommentDTO result = taskService.addComment(taskId, commentText);

        // Assert
        assertEquals(commentText, result.getText());
        verify(commentRepository, times(1)).save(any());
    }
}