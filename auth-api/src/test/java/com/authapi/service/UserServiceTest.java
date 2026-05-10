package com.authapi.service;

import com.authapi.dto.user.UserResponse;
import com.authapi.dto.user.login.UserLoginRequest;
import com.authapi.dto.user.login.UserLoginResponse;
import com.authapi.dto.user.registration.UserRegistrationRequest;
import com.authapi.exception.RegistrationException;
import com.authapi.mapper.UserMapper;
import com.authapi.model.User;
import com.authapi.repository.UserRepository;
import com.authapi.security.JwtUtil;
import com.authapi.service.impl.UserServiceImpl;
import com.authapi.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should successfully save user")
    void registerUser_ReturnsUserResponse() {
        User user = TestUtil.createUser();
        UserResponse userResponse = TestUtil.createUserResponse();
        UserRegistrationRequest registrationRequest = TestUtil.createUserRegistrationRequest();

        when(userRepository.existsByEmail(registrationRequest.email())).thenReturn(false);
        when(userMapper.toEntity(registrationRequest)).thenReturn(user);
        when(passwordEncoder.encode(registrationRequest.password())).thenReturn(user.getPassword());
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponse);

        UserResponse actual = userService.register(registrationRequest);

        assertEquals(userResponse, actual);
        verify(userMapper).toEntity(registrationRequest);
        verify(passwordEncoder).encode(registrationRequest.password());
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("Should throw exception when saving user with existing email")
    void registerUserWithExistingEmail_ThrowsException() {
        UserRegistrationRequest registrationRequest = TestUtil.createUserRegistrationRequest();

        when(userRepository.existsByEmail(registrationRequest.email())).thenReturn(true);

        assertThrows(RegistrationException.class,
                () -> userService.register(registrationRequest)
        );
        verifyNoInteractions(userMapper);
        verifyNoInteractions(passwordEncoder);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Should authenticate user and return token")
    void login_ReturnsToken() {
        UserLoginRequest request = TestUtil.createUserLoginRequest();

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("user@gmail.com");
        when(jwtUtil.generateToken("user@gmail.com")).thenReturn("jwt-token");

        UserLoginResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        verify(authenticationManager).authenticate(any());
        verify(jwtUtil).generateToken("user@gmail.com");
    }

    @Test
    @DisplayName("Should authenticate user and return token")
    void loginUser_BadCredentials_ReturnsToken() {
        UserLoginRequest request = TestUtil.createUserLoginRequest();

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid email or password"));

        assertThrows(BadCredentialsException.class, () -> {
            userService.login(request);
        });

        verifyNoInteractions(jwtUtil);
    }
}