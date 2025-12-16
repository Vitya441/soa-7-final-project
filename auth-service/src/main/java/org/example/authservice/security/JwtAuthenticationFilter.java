package org.example.authservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.authservice.model.enumeration.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {

            String username = jwtService.extractUsername(jwt);
            String roleName = jwtService.extractRole(jwt);
            Long userId = jwtService.extractUserId(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                Role userRole = Role.valueOf(roleName);

                CustomUserDetails userDetails = CustomUserDetails.builder()
                        .userId(userId)
                        .username(username)
                        .password("") // Пароль не требуется
                        .role(userRole)
                        .build();

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, // Здесь будет наш UserDetails (из JpaUserDetailsService)
                            null,        // Credentials не нужны для токена
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Запись в SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception exception) {
            // Если токен невалиден (истек, испорчен и т.д.)
            logger.warn("JWT validation failed: " + exception.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.getWriter().write("Invalid or expired JWT token");
            return; // Прерываем цепочку фильтров
        }
        filterChain.doFilter(request, response);
    }
}
