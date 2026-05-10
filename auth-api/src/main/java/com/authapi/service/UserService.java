package com.authapi.service;

import com.authapi.dto.user.UserResponse;
import com.authapi.dto.user.login.UserLoginRequest;
import com.authapi.dto.user.login.UserLoginResponse;
import com.authapi.dto.user.registration.UserRegistrationRequest;

public interface UserService {
    UserResponse register(UserRegistrationRequest request);

    UserLoginResponse login(UserLoginRequest request);
}
