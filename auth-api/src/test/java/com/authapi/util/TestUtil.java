package com.authapi.util;

import com.authapi.dto.process.ProcessRequest;
import com.authapi.dto.user.UserResponse;
import com.authapi.dto.user.login.UserLoginRequest;
import com.authapi.dto.user.registration.UserRegistrationRequest;
import com.authapi.model.User;
import java.util.UUID;

public class TestUtil {
    public static User createUser() {
        return new User()
                .setEmail("user@gmail.com")
                .setPassword("$2a$08$YDblM5qyBS4pU/bN2OH72u00XBZPvpEtkZwxsxFor38seRswBh1Je");
    }

    public static UserRegistrationRequest createUserRegistrationRequest() {
        return new UserRegistrationRequest("user@gmail.com", "12345678", "12345678");
    }

    public static UserLoginRequest createUserLoginRequest() {
        return new UserLoginRequest("user@gmail.com", "12345678");
    }

    public static UserResponse createUserResponse() {
        return new UserResponse(UUID.randomUUID(), "user@gmail.com");
    }

    public static ProcessRequest createProcessRequest() {
        return new ProcessRequest("test");
    }
}
