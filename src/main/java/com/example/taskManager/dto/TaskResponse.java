package com.example.taskManager.dto;

import com.example.taskManager.entity.TaskPriority;
import com.example.taskManager.entity.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long authorId;
    private Long assigneeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
