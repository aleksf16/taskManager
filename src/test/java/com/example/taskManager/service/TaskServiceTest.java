package com.example.taskManager.service;

import com.example.taskManager.dto.TaskRequest;
import com.example.taskManager.entity.Task;
import com.example.taskManager.entity.User;
import com.example.taskManager.repository.TaskRepository;
import com.example.taskManager.repository.UserRepository;
import com.example.taskManager.security.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    TaskService taskService;

    @Test
    void getById_whenTaskDoesNotExist_throwEntityNotFoundException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.getById(1L));
    }

    @Test
    void updateTask_whenNotAuthorOrAdmin_throwAccessDenied() {
        User user = new User();
        user.setId(2L);

        Task task = new Task();
        task.setAuthor(new User() {{
            setId(1L);
        }});

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        TaskRequest request = new TaskRequest();
        request.setTitle("Updated");

        assertThrows(AccessDeniedException.class,
                () -> taskService.updateTask(1L, request, new UserDetailsImpl(user)));
    }
}
