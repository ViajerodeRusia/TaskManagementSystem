package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.controller.dto.CommentDTO;
import com.example.TaskManagementSystem.controller.dto.TaskDTO;
import com.example.TaskManagementSystem.controller.dto.UpdateTaskDTO;
import com.example.TaskManagementSystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO newTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UpdateTaskDTO> editTask(@RequestBody UpdateTaskDTO updateTaskDTO) {
        UpdateTaskDTO updatedTask = taskService.editTask(updateTaskDTO);
        return ResponseEntity.ok(updatedTask);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }
    @PostMapping("/{id}/comments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long id, @RequestParam String comment) {
        CommentDTO newComment = taskService.addComment(id, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }
    @GetMapping("/my-tasks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TaskDTO>> getUserTasks() {
        List<TaskDTO> tasks = taskService.getUserTasks();
        return ResponseEntity.ok(tasks);
    }
    @PatchMapping("/my-tasks/{id}/status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity updateTaskStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }
    @PostMapping("/my-tasks/{id}/comments")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentDTO> addCommentUser(@PathVariable Long id, @RequestParam String comment) {
        CommentDTO newComment = taskService.addCommentUser(id, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }
}
