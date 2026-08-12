package com.example.sennaccess.data

// Repositorio de autenticación: encapsula las llamadas de sesión del backend.
// El cierre de sesión es best-effort: la app limpia su estado local aunque falle.

class AuthRepository {

    // Autentica al usuario con correo y contraseña y devuelve la respuesta del login.
    suspend fun login(email: String, password: String): LoginResponse =
        RetrofitClient.conServicio { it.login(LoginRequest(email, password)) }

    // Cierra la sesión en el servidor (registra la "Salida" y revoca el token).
    suspend fun logout(token: String): LogoutResponse =
        RetrofitClient.conServicio { it.logout("Bearer $token") }
}
