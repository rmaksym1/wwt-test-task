package com.authapi.dto.user;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email
) {}
