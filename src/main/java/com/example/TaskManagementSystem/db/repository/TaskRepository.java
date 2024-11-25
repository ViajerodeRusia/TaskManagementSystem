package com.example.TaskManagementSystem.db.repository;

import com.example.TaskManagementSystem.db.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAuthorId(Long authorId);
    Optional<Task> findByIdAndAuthorId(Long id, Long authorId);
}
