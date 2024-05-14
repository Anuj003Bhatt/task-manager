package com.abcorp.taskmanager.controller;

import com.abcorp.taskmanager.model.request.AddTaskDto;
import com.abcorp.taskmanager.model.response.ListResponse;
import com.abcorp.taskmanager.model.response.TaskDto;
import com.abcorp.taskmanager.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task")
public class TaskController {
    private final TaskService taskService;

    @Operation(
            summary = "Add new task for a user",
            description = "Create a new task for user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "404", description = "No user found for ID")
    })
    @PostMapping("users/{userId}/tasks/add")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto signup(
            @PathVariable("userId") UUID userId,
            @RequestBody @Valid AddTaskDto addTaskDto
    ) {
        return taskService.addTask(userId, addTaskDto);
    }

    @Operation(
            summary = "List User Tasks",
            description = "Find the list of all tasks of a user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of tasks in response"),
            @ApiResponse(responseCode = "404", description = "No user found for ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("users/{userid}/tasks/list")
    public ListResponse<TaskDto> listUsers(
            @PathVariable("userId") UUID userId,
            @PageableDefault(size = 20)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            }) Pageable pageable
    ) {
        return taskService.listTasksForUser(userId, pageable);
    }

    @Operation(
            summary = "Update Task",
            description = "Update a task for user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Task in response"),
            @ApiResponse(responseCode = "404", description = "No user found for ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("users/{userid}/tasks/{taskId}")
    public TaskDto updateTask(
            @PathVariable("userId") UUID userId,
            @PathVariable("taskId") UUID taskId,
            @RequestBody @Valid TaskDto task
    ) {
        return taskService.updateTask(userId, taskId, task);
    }

    @Operation(
            summary = "Delete Task",
            description = "Delete a task"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task Deleted successfully")
    })
    @DeleteMapping("tasks/{taskId}")
    public Object updateTask(
            @PathVariable("taskId") UUID taskId
    ) {
        Boolean response = taskService.deleteTask(taskId);
        if (response) {
            return Map.of("message", "Task has been deleted successfully");
        } else {
            return Map.of("message", "Unable to delete task");
        }
    }
}
