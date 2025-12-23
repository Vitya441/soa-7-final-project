package by.modsen.libraryapp.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(7)

        try {
            val userId = jwtService.extractUserId(jwt)
            val username = jwtService.extractUsername(jwt)
            val roleName = jwtService.extractRole(jwt)

            // Проверка на истечение срока (хотя Jwts.parser уже делает это, это дополнительная проверка)
            if (jwtService.isTokenExpired(jwt)) {
                throw RuntimeException("Token expired")
            }

            if (username.isNotEmpty() && SecurityContextHolder.getContext().authentication == null) {
                val principal = UserPrincipal(id = userId, username = username, role = roleName ?: "READER")
                val authorities = listOf(SimpleGrantedAuthority("ROLE_${principal.role}"))

                val authToken = UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    authorities
                )

                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        } catch (ex: Exception) {
            logger.warn("JWT validation failed in Core Service: ${ex.message}")
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Invalid or expired JWT token")
            return
        }

        filterChain.doFilter(request, response)
    }
}
