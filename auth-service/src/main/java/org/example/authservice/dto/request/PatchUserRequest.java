package org.example.authservice.dto.request;

import org.example.authservice.model.enumeration.Role;

public record PatchUserRequest(
        String username,
        String password,
        String firstName,
        String lastName,
        Role role
) {
}