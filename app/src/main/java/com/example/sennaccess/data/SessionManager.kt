package com.example.sennaccess.data

// Guarda en memoria los datos de la sesión activa (token y perfil del usuario).
// No persiste en disco: la sesión se pierde al reiniciar la app, por diseño.

object SessionManager {
    // Datos de la sesión actual. Los setters son privados: solo pueden modificarse a
    // través de los métodos de este objeto para evitar estados inconsistentes.
    var token: String? = null
        private set
    var userName: String? = null
        private set
    var userEmail: String? = null
        private set
    var userRole: String? = null
        private set

    // Almacena la sesión completa tras un login exitoso.
    fun saveSession(token: String?, name: String?, email: String?, role: String?) {
        this.token = token
        this.userName = name
        this.userEmail = email
        this.userRole = role
    }

    // Actualiza solo el token, por ejemplo cuando se renueva.
    fun saveToken(token: String?) {
        this.token = token
    }

    // Cierra la sesión y limpia todos los datos; se usa al hacer logout.
    fun clear() {
        token = null
        userName = null
        userEmail = null
        userRole = null
    }

    // Construye el header HTTP de autorización con el esquema Bearer a partir del token
    // guardado. Devuelve null si no hay sesión (se usa para decidir el fallback a mocks).
    fun authHeader(): String? = token?.let { "Bearer $it" }
}