package com.authapi.controller;

import com.authapi.dto.user.login.UserLoginRequest;
import com.authapi.dto.user.registration.UserRegistrationRequest;
import com.authapi.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.authapi.util.TestConstants.ADD_USER_DB_PATH;
import static com.authapi.util.TestConstants.CLEANUP_DB_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private static final String AUTH_REGISTRATION_PATH = "/auth/register";
    private static final String AUTH_LOGIN_PATH = "/auth/login";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return user response on registration")
    @Sql(scripts = CLEANUP_DB_PATH, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void registration_ValidRequest_ReturnsResponse() throws Exception {
        UserRegistrationRequest request = TestUtil.createUserRegistrationRequest();

        mockMvc.perform(post(AUTH_REGISTRATION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DisplayName("Should return conflict when registrating with existing email")
    @Sql(scripts = CLEANUP_DB_PATH, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = ADD_USER_DB_PATH, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void registrationWithNonUniqueEmail_ReturnsBadRequest() throws Exception {
        UserRegistrationRequest request = TestUtil.createUserRegistrationRequest();

        mockMvc.perform(post(AUTH_REGISTRATION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return token on registration")
    @Sql(scripts = CLEANUP_DB_PATH, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = ADD_USER_DB_PATH, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void login_ValidRequest_ReturnsToken() throws Exception {
        UserLoginRequest request = TestUtil.createUserLoginRequest();

        mockMvc.perform(post(AUTH_LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("Should return unathorized on incorrect login data")
    @Sql(scripts = CLEANUP_DB_PATH, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = ADD_USER_DB_PATH, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void loginWithIncorrectData_ReturnsUnathorized() throws Exception {
        UserLoginRequest request = new UserLoginRequest("user@gmail.com", "incorrect");

        mockMvc.perform(post(AUTH_LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
