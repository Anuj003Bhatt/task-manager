package com.abcorp.taskmanager.service.task;

import com.abcorp.taskmanager.model.request.AddTaskDto;
import com.abcorp.taskmanager.model.response.ListResponse;
import com.abcorp.taskmanager.model.response.TaskDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TaskService {
    ListResponse<TaskDto> listTasksForUser(UUID userId, Pageable pageable);
    TaskDto addTask(UUID userId, AddTaskDto addTaskDto);
    TaskDto getTaskById(UUID taskId, UUID userId);
    TaskDto updateTask(UUID userId, UUID taskId, TaskDto taskDto);
    Boolean deleteTask(UUID userId, UUID taskId);
    ListResponse<TaskDto> listTasksForUserByStatusAndPriority(UUID userId, Integer status, Integer priority, Pageable pageable);
}
