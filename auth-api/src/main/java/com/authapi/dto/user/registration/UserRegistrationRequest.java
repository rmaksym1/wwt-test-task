package com.authapi.dto.user.registration;

import com.authapi.validation.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.experimental.Accessors;

@FieldMatch(
        field = "password",
        fieldToMatch = "repeatPassword",
        message = "Password and repeat password must be same!"
)
public record UserRegistrationRequest(
        @NotBlank(message = "Email cannot be blank!")
        @Size(min = 8, max = 30, message = "Email must be between 8 and 30 characters long!")
        String email,
        @NotBlank(message = "Password cannot be blank!")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters long!")
        String password,
        @NotBlank(message = "Repeat password cannot be blank!")
        @Size(min = 8, max = 20, message = "Repeat password must be between 8 and 20 characters long!")
        String repeatPassword
) {}
