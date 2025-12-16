package org.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.dto.request.LoginRequest;
import org.example.authservice.dto.request.RegisterRequest;
import org.example.authservice.model.entity.User;
import org.example.authservice.model.enumeration.Role;
import org.example.authservice.repository.UserRepository;
import org.example.authservice.security.CustomUserDetails;
import org.example.authservice.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    @Transactional
    public String register(RegisterRequest request) {
        if (repository.existsByUsername(request.username())) {
            throw new RuntimeException("User with this username already exists");
        }
        User user = User.builder()
                .username(request.username())
                .password(encoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(Role.USER)
                .build();
        user = repository.save(user);
        CustomUserDetails userDetails = buildUserDetails(user);

        return jwtService.generateAccessToken(userDetails, user.getId());
    }

    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return jwtService.generateAccessToken(user, user.getUserId());
    }

    private CustomUserDetails buildUserDetails(User user) {
        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), Role.USER);
    }
}
