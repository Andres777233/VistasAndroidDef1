package com.example.sennaccess.data

// Repositorio de novedades: lista las publicadas por el usuario actual y el catálogo
// general consultable por admin e instructor.

class NovedadRepository {

    // Listado general de novedades (admin/instructor), con búsqueda opcional.
    suspend fun getNovedades(token: String): List<Novedad> =
        RetrofitClient.conServicio { it.getNovedades("Bearer $token") }

    // Novedades publicadas por el usuario logueado.
    suspend fun getMyNovedades(token: String): List<Novedad> =
        RetrofitClient.conServicio { it.getMyNovedades("Bearer $token") }
}
