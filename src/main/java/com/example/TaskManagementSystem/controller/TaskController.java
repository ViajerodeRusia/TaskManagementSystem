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

/**
 * REST-контроллер для управления задачами.
 */
@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    /**
     * Создает новую задачу.
     *
     * @param taskDTO объект передачи данных задачи
     * @return созданная задача
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO newTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    /**
     * Редактирует существующую задачу.
     *
     * @param updateTaskDTO обновленный объект передачи данных задачи
     * @return обновленная задача
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UpdateTaskDTO> editTask(@RequestBody UpdateTaskDTO updateTaskDTO) {
        UpdateTaskDTO updatedTask = taskService.editTask(updateTaskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Удаляет задачу по ID.
     *
     * @param id ID задачи для удаления
     * @return статус ответа
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }

    /**
     * Добавляет комментарий к задаче.
     *
     * @param id ID задачи
     * @param comment текст комментария
     * @return созданный комментарий
     */
    @PostMapping("/{id}/comments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long id, @RequestParam String comment) {
        CommentDTO newComment = taskService.addComment(id, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }

    /**
     * Возвращает все задачи.
     *
     * @return список задач
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * Возвращает задачи, назначенные текущему пользователю.
     *
     * @return список задач пользователя
     */
    @GetMapping("/my-tasks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TaskDTO>> getUserTasks() {
        List<TaskDTO> tasks = taskService.getUserTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Обновляет статус задачи пользователя.
     *
     * @param id ID задачи
     * @param status новый статус
     * @return статус ответа
     */
    @PatchMapping("/my-tasks/{id}/status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity updateTaskStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    /**
     * Добавляет комментарий к задаче пользователя.
     *
     * @param id ID задачи
     * @param comment текст комментария
     * @return созданный комментарий
     */
    @PostMapping("/my-tasks/{id}/comments")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentDTO> addCommentUser(@PathVariable Long id, @RequestParam String comment) {
        CommentDTO newComment = taskService.addCommentUser(id, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }
}
