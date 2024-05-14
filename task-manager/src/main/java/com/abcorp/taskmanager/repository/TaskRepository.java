package com.abcorp.taskmanager.repository;

import com.abcorp.taskmanager.model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findAllByUserId(UUID userId, Pageable pageable);
    Optional<Task> findByIdAndUserId(UUID taskId, UUID userId);
}
