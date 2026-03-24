package com.example.taskManager.service;

import com.example.taskManager.dto.TaskRequest;
import com.example.taskManager.dto.TaskResponse;
import com.example.taskManager.entity.*;
import com.example.taskManager.repository.TaskRepository;
import com.example.taskManager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(TaskRequest request, User currentUser) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(
                request.getStatus() != null ? request.getStatus() : TaskStatus.TODO
        );
        task.setPriority(
                request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM
        );
        task.setAuthor(currentUser);

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    public TaskResponse getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        return toResponse(task);
    }

    public TaskResponse updateTask(Long id, TaskRequest request, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (isAuthorOrAdmin(task, currentUser)) {
            throw new AccessDeniedException("Not allowed");
        }

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }

        Task updated = taskRepository.save(task);
        return toResponse(updated);
    }

    public void deleteTask(Long id, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        if (isAuthorOrAdmin(task, currentUser)) {
            throw new AccessDeniedException("Not allowed");
        }
        taskRepository.delete(task);
    }

    public List<TaskResponse> findTasks(TaskStatus status, Long authorId, Long assigneeId) {
        List<Task> tasks;

        if (status == null && authorId == null && assigneeId == null) {
            tasks = taskRepository.findAll();
        } else {
            tasks = taskRepository.findAll().stream()
                    .filter(t -> status == null || t.getStatus() == status)
                    .filter(t -> authorId == null ||
                            (t.getAuthor() != null && t.getAuthor().getId().equals(authorId)))
                    .filter(t -> assigneeId == null ||
                            (t.getAssignee() != null && t.getAssignee().getId().equals(assigneeId)))
                    .collect(Collectors.toList());
        }

        return tasks.stream().map(this::toResponse).toList();
    }

    private boolean isAuthorOrAdmin(Task task, User user) {
        return !task.getAuthor().getId().equals(user.getId())
                && user.getRole() != Role.ADMIN;
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse dto = new TaskResponse();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setAuthorId(task.getAuthor() != null ? task.getAuthor().getId() : null);
        dto.setAssigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null);
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }
}
