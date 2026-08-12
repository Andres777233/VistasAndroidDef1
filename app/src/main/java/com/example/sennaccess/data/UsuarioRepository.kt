package com.example.sennaccess.data

// Repositorio de usuarios: expone las llamadas de la API que operan sobre perfiles y
// roles, siempre mediante el fallback USB/WiFi de RetrofitClient.

class UsuarioRepository {

    // Lista todos los usuarios del sistema (requiere rol admin o instructor).
    suspend fun getUsers(token: String): List<UsuarioApi> =
        RetrofitClient.conServicio { it.getUsers("Bearer $token") }

    // Devuelve el perfil del usuario autenticado.
    suspend fun getCurrentUser(token: String): UsuarioApi =
        RetrofitClient.conServicio { it.getCurrentUser("Bearer $token") }

    // Obtiene el catálogo de roles, útil para filtros y desplegables.
    suspend fun getRoles(token: String): List<Role> =
        RetrofitClient.conServicio { it.getRoles("Bearer $token") }
}
