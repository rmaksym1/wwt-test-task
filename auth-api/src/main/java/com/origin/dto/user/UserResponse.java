package com.origin.dto.user;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email
) {}
