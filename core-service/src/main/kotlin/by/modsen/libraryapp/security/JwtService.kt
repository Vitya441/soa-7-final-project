package by.modsen.libraryapp.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtService {
    @Value("\${app.jwt.secretKey}")
    private val secretKey: String? = null

    @Value("\${app.jwt.expiration}")
    private val jwtExpiration: Long = 0 // Время жизни access токена

    fun extractUsername(token: String): String =
        extractClaim(token, Claims::getSubject)

    // Извлечение ID пользователя (кастомный claim "id")
    fun extractUserId(token: String): Long? = extractClaim(token) { claims ->
        val number = claims.get("userId", Number::class.java)
        number?.toLong()
    }

    fun extractRole(token: String): String? = extractClaim(token) { claims ->
        claims.get("role", String::class.java)
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts
            .parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    private fun getSignInKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date = extractClaim(token, Claims::getExpiration)
}
