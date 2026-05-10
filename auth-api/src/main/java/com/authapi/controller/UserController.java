package com.authapi.controller;

import com.authapi.dto.user.UserResponse;
import com.authapi.dto.user.login.UserLoginRequest;
import com.authapi.dto.user.login.UserLoginResponse;
import com.authapi.dto.user.registration.UserRegistrationRequest;
import com.authapi.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    UserResponse register(@RequestBody @Valid UserRegistrationRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    UserLoginResponse login(@RequestBody @Valid UserLoginRequest request) {
        return userService.login(request);
    }
}
