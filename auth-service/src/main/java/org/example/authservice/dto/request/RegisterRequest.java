package org.example.authservice.dto.request;

public record RegisterRequest(
        String username,
        String password,
        String firstName,
        String lastName
) {
}