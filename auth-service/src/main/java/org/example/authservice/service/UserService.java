package org.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.dto.request.CreateUserRequest;
import org.example.authservice.dto.request.PatchUserRequest;
import org.example.authservice.dto.response.UserResponse;
import org.example.authservice.mapper.UserMapper;
import org.example.authservice.model.entity.User;
import org.example.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для Админов, чтобы управлять пользователями
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        User user = mapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user = repository.save(user);

        return mapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        List<User> allEntities = repository.findAll();
        return allEntities.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = getUserByIdOrThrow(id);
        return mapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = getUserByUsernameOrThrow(username);
        return mapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateById(Long id, CreateUserRequest request) {
        User existingUser = getUserByIdOrThrow(id);
        mapper.updateEntityFromDto(request, existingUser);
        existingUser.setPassword(passwordEncoder.encode(request.password()));

        return mapper.toResponse(existingUser);
    }

    @Transactional
    public UserResponse patchUserById(Long id, PatchUserRequest request) {
        User existingUser = getUserByIdOrThrow(id);
        mapper.patchEntityFromDto(request, existingUser);
        if (request.password() != null) {
            existingUser.setPassword(passwordEncoder.encode(request.password()));
        }

        return mapper.toResponse(existingUser);
    }

    @Transactional
    public void deleteUserById(Long id) {
        User user = getUserByIdOrThrow(id);
        repository.delete(user);
    }

    private User getUserByIdOrThrow(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User with id not found"));
    }

    private User getUserByUsernameOrThrow(String username) {
        return repository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User with username not found"));
    }
}
