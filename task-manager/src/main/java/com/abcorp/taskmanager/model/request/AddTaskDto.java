package com.abcorp.taskmanager.model.request;

import com.abcorp.taskmanager.type.TaskPriority;
import com.abcorp.taskmanager.type.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddTaskDto {
    @NotBlank(message = "Task must have a title")
    private String title;
    @NotBlank(message = "Task must have a description")
    private String description;
    private TaskStatus status = TaskStatus.TODO;
    @NotNull(message = "Task must have a priority")
    private TaskPriority priority;
}
