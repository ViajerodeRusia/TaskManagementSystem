package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.controller.dto.CommentDTO;
import com.example.TaskManagementSystem.controller.dto.TaskDTO;
import com.example.TaskManagementSystem.controller.dto.UpdateTaskDTO;
import com.example.TaskManagementSystem.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        when(taskService.createTask(taskDTO)).thenReturn(taskDTO);

        mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    void editTask_ShouldReturnUpdatedTask() throws Exception {
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO();
        when(taskService.editTask(updateTaskDTO)).thenReturn(updateTaskDTO);

        mockMvc.perform(put("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTask_ShouldReturnOk() throws Exception {
        when(taskService.deleteTask(1L)).thenReturn(HttpStatus.OK);

        mockMvc.perform(delete("/api/task/1"))
                .andExpect(status().isOk());
    }

    @Test
    void addComment_ShouldReturnCreatedComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        when(taskService.addComment(1L, "Test comment")).thenReturn(commentDTO);

        mockMvc.perform(post("/api/task/1/comments")
                        .param("comment", "Test comment"))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllTasks_ShouldReturnTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/task"))
                .andExpect(status().isOk());
    }

    @Test
    void getUserTasks_ShouldReturnUserTasks() throws Exception {
        when(taskService.getUserTasks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/task/my-tasks"))
                .andExpect(status().isOk());
    }

    @Test
    void updateTaskStatus_ShouldReturnOk() throws Exception {
        when(taskService.updateTaskStatus(1L, "COMPLETED")).thenReturn(HttpStatus.OK);

        mockMvc.perform(patch("/api/task/my-tasks/1/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk());
    }

    @Test
    void addCommentUser_ShouldReturnCreatedComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        when(taskService.addCommentUser(1L, "Test comment")).thenReturn(commentDTO);

        mockMvc.perform(post("/api/task/my-tasks/1/comments")
                        .param("comment", "Test comment"))
                .andExpect(status().isCreated());
    }
}