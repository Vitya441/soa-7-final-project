package by.modsen.libraryapp.security

data class UserPrincipal(
    val id: Long?,
    val username: String,
    val role: String
)