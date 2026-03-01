package com.manoj.hireflow.controller;

import com.manoj.hireflow.entity.User;
import com.manoj.hireflow.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manoj.hireflow.dto.UserRegisterRequest;
import com.manoj.hireflow.dto.UserResponse;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(
            @Valid @RequestBody UserRegisterRequest request) {
        User savedUser = userService.register(request);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());

        return response;
    }
}

