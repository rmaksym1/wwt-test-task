package com.authapi.mapper;

import com.authapi.config.MapperConfig;
import com.authapi.dto.user.UserResponse;
import com.authapi.dto.user.registration.UserRegistrationRequest;
import com.authapi.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponse toDto(User user);

    User toEntity(UserRegistrationRequest userResponse);
}
