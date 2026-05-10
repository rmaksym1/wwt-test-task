package com.origin.service;

import com.origin.dto.user.UserResponse;
import com.origin.dto.user.login.UserLoginRequest;
import com.origin.dto.user.login.UserLoginResponse;
import com.origin.dto.user.registration.UserRegistrationRequest;

public interface UserService {
    UserResponse register(UserRegistrationRequest request);

    UserLoginResponse login(UserLoginRequest request);
}
