package com.example.sennaccess.data

// Interfaz que declara los endpoints REST del backend como funciones suspend de Retrofit.
// Cada anotación (@GET/@POST) se resuelve contra la baseUrl de RetrofitClient:
// baseUrl + "login" equivale a http://127.0.0.1:8000/api/login (o la IP WiFi).

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // ---- Auth ----
    // 1. Login (POST /api/login): envía email y contraseña en el cuerpo de la petición.
    //    Es el único endpoint público, no requiere token. Devuelve el usuario, su rol
    //    y el access_token que la app guardará para autenticar el resto de llamadas.
    @POST("login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    // 1b. POST /api/logout: cierra la sesión en el servidor. Requiere el token Bearer.
    //    Registra la "Salida" del usuario en el historial antes de invalidar el token.
    @POST("logout")
    suspend fun logout(@Header("Authorization") auth: String): LogoutResponse

    // ---- Cualquier rol (sesión) ----
    // 2. GET /api/user: perfil del usuario autenticado. Cualquier rol con sesión puede
    //    pedir sus propios datos; el token viaja en el header "Authorization: Bearer".
    @GET("user")
    suspend fun getCurrentUser(@Header("Authorization") auth: String): UsuarioApi

    // 3. GET /api/my-ingresos: registros de ingreso del usuario logueado. Listado
    //    personal, disponible para cualquier rol autenticado.
    @GET("my-ingresos")
    suspend fun getMyIngresos(@Header("Authorization") auth: String): List<Ingreso>

    // 4. GET /api/my-equipment: equipos registrados a nombre del usuario actual.
    @GET("my-equipment")
    suspend fun getMyEquipment(@Header("Authorization") auth: String): List<IngresoEquipo>

    // ---- Admin / Instructor ----
    // 5. Secciones administrativas: requieren rol admin o instructor y el token Bearer.
    @GET("admin/users")
    suspend fun getUsers(@Header("Authorization") auth: String): List<UsuarioApi>

    // 6. GET /api/admin/ingresos: registro global de ingresos de todos los usuarios.
    @GET("admin/ingresos")
    suspend fun getIngresos(@Header("Authorization") auth: String): List<Ingreso>

    // 7. GET /api/admin/roles: catálogo de roles, útil para filtros y desplegables.
    @GET("admin/roles")
    suspend fun getRoles(@Header("Authorization") auth: String): List<Role>

    // 8. GET /api/my-novedades: novedades publicadas por el usuario logueado.
    @GET("my-novedades")
    suspend fun getMyNovedades(@Header("Authorization") auth: String): List<Novedad>

    // 9. GET /api/novedades: listado general de novedades con búsqueda opcional por
    //    texto, enviada como parámetro de consulta "search".
    @GET("novedades")
    suspend fun getNovedades(
        @Header("Authorization") auth: String,
        @Query("search") search: String? = null
    ): List<Novedad>

    // ---- Solo Admin ----
    // 10. GET /api/admin/equipment: inventario completo de equipos registrados en
    //     ingresos. Acceso restringido al rol admin.
    @GET("admin/equipment")
    suspend fun getEquipment(@Header("Authorization") auth: String): List<IngresoEquipo>

    // 11. POST /api/admin/equipment: registra el ingreso de un equipo. Disponible para
    //     admin e instructor; el backend asigna el usuario desde el token.
    @POST("admin/equipment")
    suspend fun createEquipment(
        @Header("Authorization") auth: String,
        @Body body: IngresoEquipoRequest
    ): EquipmentResponse
}
