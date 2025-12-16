package org.example.authservice.dto.request;

import org.example.authservice.model.enumeration.Role;

/**
 *  Для Админа
 */
public record CreateUserRequest(
        String username,
        String password,
        String firstName,
        String lastName,
        Role role
) {
}