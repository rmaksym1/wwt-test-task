package com.authapi.service.impl;

import com.authapi.dto.user.UserResponse;
import com.authapi.dto.user.login.UserLoginRequest;
import com.authapi.dto.user.login.UserLoginResponse;
import com.authapi.dto.user.registration.UserRegistrationRequest;
import com.authapi.exception.RegistrationException;
import com.authapi.mapper.UserMapper;
import com.authapi.model.User;
import com.authapi.repository.UserRepository;
import com.authapi.security.JwtUtil;
import com.authapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponse register(@RequestBody @Valid UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RegistrationException("User by email: " + request.email() + " already exists!");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserLoginResponse login(@RequestBody @Valid UserLoginRequest request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponse(token);
    }
}
