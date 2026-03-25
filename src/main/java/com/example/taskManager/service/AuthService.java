package com.example.taskManager.service;

import com.example.taskManager.dto.AuthRequest;
import com.example.taskManager.dto.AuthResponse;
import com.example.taskManager.dto.RegisterRequest;
import com.example.taskManager.entity.Role;
import com.example.taskManager.entity.User;
import com.example.taskManager.repository.UserRepository;
import com.example.taskManager.security.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String jwt = jwtService.generateToken(new UserDetailsImpl(user));

        return new AuthResponse(jwt, user.getUsername(), user.getRole(), user.getEmail());
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if (user.isPresent()) {
            String jwt = jwtService.generateToken(new UserDetailsImpl(user.get()));
            return new AuthResponse(jwt, user.get().getUsername(), user.get().getRole(), user.get().getEmail());
        } else {
            throw new EntityNotFoundException();
        }

    }
}