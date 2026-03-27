package com.example.taskManager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email(message = "Email must be valid")
    private String email;
}
