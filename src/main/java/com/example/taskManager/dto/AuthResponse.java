package com.example.taskManager.dto;

import com.example.taskManager.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthResponse {
    private String token;
    private String username;
    private Role role;
}
