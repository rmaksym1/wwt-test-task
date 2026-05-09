package com.origin.controller;

import com.origin.dto.user.UserResponse;
import com.origin.dto.user.login.UserLoginRequest;
import com.origin.dto.user.login.UserLoginResponse;
import com.origin.dto.user.registration.UserRegistrationRequest;
import com.origin.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse register(UserRegistrationRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    UserLoginResponse login(UserLoginRequest request) {
        return userService.login(request);
    }
}
