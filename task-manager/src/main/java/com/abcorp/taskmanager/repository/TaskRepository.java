package com.abcorp.taskmanager.repository;

import com.abcorp.taskmanager.model.entity.Task;
import com.abcorp.taskmanager.type.TaskPriority;
import com.abcorp.taskmanager.type.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findAllByUserId(UUID userId, Pageable pageable);
    Page<Task> findAllByUserIdAndStatusAndPriority(UUID userId, TaskStatus status, TaskPriority priority, Pageable pageable);
    Page<Task> findAllByUserIdAndStatus(UUID userId, TaskStatus status, Pageable pageable);
    Page<Task> findAllByUserIdAndPriority(UUID userId, TaskPriority priority, Pageable pageable);

    Optional<Task> findByIdAndUserId(UUID taskId, UUID userId);
}
