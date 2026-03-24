package com.example.taskManager.controller;

import com.example.taskManager.entity.User;
import com.example.taskManager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    public final UserService userService;

    @GetMapping("/users")
    public List<User> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.loadAllUsers();
    }
}
