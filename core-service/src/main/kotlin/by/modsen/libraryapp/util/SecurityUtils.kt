package by.modsen.libraryapp.util

import by.modsen.libraryapp.security.UserPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
object SecurityUtils {

    // todo: Не нравиться что везде фигурирует Long? тип , т.к nullable этот тип возвращается в JwtService
    fun getCurrentUserId(): Long? {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication == null || !authentication.isAuthenticated) {
            throw RuntimeException("Пользователь не аунтефицирован")
        }

        val principal = authentication.principal
        return if (principal is UserPrincipal) {
            principal.id
        } else {
            throw RuntimeException("Не удалось получить ID пользователя из контекста")
        }
    }

    fun getCurrentUsername(): String? {
        val principal = SecurityContextHolder.getContext().authentication?.principal
        return (principal as? UserPrincipal)?.username
    }
}