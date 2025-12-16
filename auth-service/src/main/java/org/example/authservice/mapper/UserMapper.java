package org.example.authservice.mapper;

import org.example.authservice.dto.request.CreateUserRequest;
import org.example.authservice.dto.request.PatchUserRequest;
import org.example.authservice.dto.response.UserResponse;
import org.example.authservice.model.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(CreateUserRequest dto);

    UserResponse toResponse(User entity);

    void updateEntityFromDto(CreateUserRequest dto, @MappingTarget User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntityFromDto(PatchUserRequest dto, @MappingTarget User entity);
}
