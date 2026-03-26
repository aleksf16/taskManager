package com.example.taskManager.service;

import com.example.taskManager.dto.AuthRequest;
import com.example.taskManager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtService jwtService;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    AuthService authService;

    @Test
    void authenticate_whenUserNotExists_throwEntityNotFoundException() {
        AuthRequest request = new AuthRequest();
        request.setUsername("exists");
        request.setPassword("123");

        assertThrows(EntityNotFoundException.class,
                () -> authService.authenticate(request));
    }
}