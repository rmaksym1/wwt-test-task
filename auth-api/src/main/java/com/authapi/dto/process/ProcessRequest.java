package com.authapi.dto.process;

import jakarta.validation.constraints.NotEmpty;

public record ProcessRequest(
        @NotEmpty(message = "Process text cannot be empty!")
        String text
) {}
