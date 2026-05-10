package com.origin.service.impl;

import com.origin.dto.user.UserResponse;
import com.origin.dto.user.login.UserLoginRequest;
import com.origin.dto.user.login.UserLoginResponse;
import com.origin.dto.user.registration.UserRegistrationRequest;
import com.origin.exception.RegistrationException;
import com.origin.mapper.UserMapper;
import com.origin.model.User;
import com.origin.repository.UserRepository;
import com.origin.security.JwtUtil;
import com.origin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RegistrationException("User by email: " + request.email() + " already exists!");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponse(token);
    }
}
