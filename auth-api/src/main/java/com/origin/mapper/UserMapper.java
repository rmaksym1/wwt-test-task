package com.origin.mapper;

import com.origin.config.MapperConfig;
import com.origin.dto.user.UserResponse;
import com.origin.dto.user.registration.UserRegistrationRequest;
import com.origin.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponse toDto(User user);

    User toEntity(UserRegistrationRequest userResponse);
}
