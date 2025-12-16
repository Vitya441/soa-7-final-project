package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.authservice.dto.request.CreateUserRequest;
import org.example.authservice.dto.request.PatchUserRequest;
import org.example.authservice.dto.response.UserResponse;
import org.example.authservice.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Админ будет через панельку создавать пользователей (библиотекаря)
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public UserResponse create(@RequestBody CreateUserRequest request) {
        return service.createUser(request);
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return service.getAllUsers();
    }

    @GetMapping("{id}")
    public UserResponse getById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping("/username/{username}")
    public UserResponse getByUsername(@PathVariable String username) {
        return service.getUserByUsername(username);
    }

    @PutMapping("{id}")
    public UserResponse updateById(@PathVariable Long id, @RequestBody CreateUserRequest request) {
        return service.updateById(id, request);
    }

    @PatchMapping("{id}")
    public UserResponse patchById(@PathVariable Long id, @RequestBody PatchUserRequest request) {
        return service.patchUserById(id, request);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteUserById(id);
    }
}
