package org.example.authservice.dto.response;

import org.example.authservice.model.enumeration.Role;

/**
 *  Для Админа
 */
public record UserResponse(
        Long id,
        String username,
        // todo: raw password?
        String password,
        String firstName,
        String lastName,
        Role role
) {
}