package com.example.sennaccess.data

// Repositorio de ingresos de personas: separa el listado personal del registro global.

class IngresoRepository {

    // Ingresos del usuario logueado (visible para cualquier rol con sesión).
    suspend fun getMyIngresos(token: String): List<Ingreso> =
        RetrofitClient.conServicio { it.getMyIngresos("Bearer $token") }

    // Registro global de ingresos de todos los usuarios (requiere admin o instructor).
    suspend fun getIngresos(token: String): List<Ingreso> =
        RetrofitClient.conServicio { it.getIngresos("Bearer $token") }
}
