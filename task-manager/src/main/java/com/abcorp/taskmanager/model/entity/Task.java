package com.abcorp.taskmanager.model.entity;

import com.abcorp.taskmanager.model.base.DtoBridge;
import com.abcorp.taskmanager.model.response.TaskDto;
import com.abcorp.taskmanager.type.TaskPriority;
import com.abcorp.taskmanager.type.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "tasks")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseAuditable implements DtoBridge<TaskDto> {
    @Id
    @GeneratedValue
    @Column(name = "id")
    @UuidGenerator
    private UUID id;

    @Column(name = "title", nullable = false)
    @NotEmpty
    private String title;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Override
    public TaskDto toDto() {
        return TaskDto
                .builder()
                .id(id)
                .title(title)
                .description(description)
                .status(status)
                .priority(priority)
                .build();
    }
}
