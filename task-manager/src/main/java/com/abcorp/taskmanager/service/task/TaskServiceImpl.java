package com.abcorp.taskmanager.service.task;

import com.abcorp.taskmanager.exception.BadRequestException;
import com.abcorp.taskmanager.exception.NotFoundException;
import com.abcorp.taskmanager.model.entity.Task;
import com.abcorp.taskmanager.model.entity.User;
import com.abcorp.taskmanager.model.request.AddTaskDto;
import com.abcorp.taskmanager.model.response.ListResponse;
import com.abcorp.taskmanager.model.response.TaskDto;
import com.abcorp.taskmanager.repository.TaskRepository;
import com.abcorp.taskmanager.repository.UserRepository;
import com.abcorp.taskmanager.type.TaskPriority;
import com.abcorp.taskmanager.type.TaskStatus;
import com.abcorp.taskmanager.type.UserStatus;
import com.abcorp.taskmanager.util.BridgeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public ListResponse<TaskDto> listTasksForUser(UUID userId, Pageable pageable) {
        Page<Task> page = taskRepository.findAllByUserId(userId, pageable);
        return BridgeUtil.buildPaginatedResponse(page);
    }

    @Override
    public TaskDto addTask(UUID userId, AddTaskDto addTaskDto) {
        if (userId == null) {
            log.error("user ID passed in for update was blank");
            throw new BadRequestException("User ID for update cannot be blank");
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    log.error("No user found for ID {}", userId);
                    return new NotFoundException("No user found for ID %s", userId);
                }
        );
        Task task = Task
                .builder()
                .title(addTaskDto.getTitle())
                .description(addTaskDto.getDescription())
                .status(addTaskDto.getStatus())
                .priority(addTaskDto.getPriority())
                .user(user)
                .build();
        return taskRepository.save(task).toDto();
    }

    @Override
    public TaskDto getTaskById(UUID taskId) {
        return taskRepository.findById(taskId).orElseThrow(
                () -> new NotFoundException("No task found for ID '%s'", taskId)
        ).toDto();
    }

    @Override
    public TaskDto updateTask(UUID userId, UUID taskId, TaskDto taskDto) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId).orElseThrow(
                () -> {
                    log.error("No task found for user {} with task id {}", userId, taskId);
                    return new NotFoundException("No task found for user %s with task id %s", userId, taskId);
                }
        );
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        return taskRepository.save(task).toDto();
    }

    @Override
    public Boolean deleteTask(UUID userId, UUID taskId) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId).orElseThrow(
                () -> {
                    log.error("No task found for ID {}", taskId);
                    return new NotFoundException("No task found for ID {}", taskId);
                }
        );
        taskRepository.delete(task);
        return true;
    }

    @Override
    public ListResponse<TaskDto> listTasksForUserByStatusAndPriority(UUID userId, Integer status, Integer priority, Pageable pageable) {
        TaskStatus taskStatus = null;
        if ( status != null && status != -1) {
            taskStatus = switch (status) {
                case 0 -> TaskStatus.TODO;
                case 1 -> TaskStatus.INPROGRESS;
                case 2 -> TaskStatus.DONE;
                default -> throw new BadRequestException("Invalid task status");
            };
        }

        TaskPriority taskPriority = null;
        if ( priority != null && priority != -1) {
            taskPriority = switch (priority) {
                case 0 -> TaskPriority.LOW;
                case 1 -> TaskPriority.MEDIUM;
                case 2 -> TaskPriority.HIGH;
                case 3 -> TaskPriority.CRITICAL;
                case 4 -> TaskPriority.BLOCKER;
                default -> throw new BadRequestException("Invalid task priority");
            };
        }

        if (taskStatus == null && taskPriority == null) {
            return listTasksForUser(userId, pageable);
        }

        if (taskStatus != null && taskPriority != null) {
            Page<Task> page = taskRepository.findAllByUserIdAndStatusAndPriority(userId, taskStatus, taskPriority, pageable);
            return BridgeUtil.buildPaginatedResponse(page);
        }

        if (taskStatus != null) {
            Page<Task> page = taskRepository.findAllByUserIdAndStatus(userId, taskStatus, pageable);
            return BridgeUtil.buildPaginatedResponse(page);
        }

        Page<Task> page = taskRepository.findAllByUserIdAndPriority(userId, taskPriority, pageable);
        return BridgeUtil.buildPaginatedResponse(page);
    }
}
