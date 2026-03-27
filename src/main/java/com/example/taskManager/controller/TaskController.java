package com.example.taskManager.controller;

import com.example.taskManager.dto.TaskRequest;
import com.example.taskManager.dto.TaskResponse;
import com.example.taskManager.security.UserDetailsImpl;
import com.example.taskManager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public TaskResponse create(@Valid @RequestBody TaskRequest request,
                               @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return taskService.createTask(request, currentUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public TaskResponse getById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public TaskResponse update(@PathVariable Long id,
                               @Valid @RequestBody TaskRequest request,
                               @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return taskService.updateTask(id, request, currentUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void delete(@PathVariable Long id,
                       @AuthenticationPrincipal UserDetailsImpl currentUser) {
        taskService.deleteTask(id, currentUser);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<TaskResponse> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long assigneeId) {
        return taskService.findTasks(status, authorId, assigneeId);
    }
}
