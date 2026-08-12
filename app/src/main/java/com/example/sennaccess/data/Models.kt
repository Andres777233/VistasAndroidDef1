package com.example.sennaccess.data

// Este archivo concentra las data classes que modelan el JSON de la API y las entidades
// de la app. @SerializedName mapea cada campo JSON (snake_case del backend Laravel)
// con la propiedad Kotlin correspondiente.

import com.google.gson.annotations.SerializedName

// Credenciales enviadas al endpoint de login. Los nombres de campo respetan el formato
// que espera el backend (user_email / user_password).
data class LoginRequest(
    @SerializedName("user_email") val user_email: String,
    @SerializedName("user_password") val user_password: String
)

// Usuario tal como lo devuelve el endpoint de login: perfil básico con su rol.
data class User(
    @SerializedName("id_usuario") val id_usuario: Int? = null,
    @SerializedName("user_identification") val user_identification: String? = null,
    @SerializedName("user_name") val user_name: String? = null,
    @SerializedName("user_lastname") val user_lastname: String? = null,
    @SerializedName("user_email") val user_email: String? = null,
    @SerializedName("user_coursenumber") val user_coursenumber: Int? = null,
    @SerializedName("user_program") val user_program: String? = null,
    @SerializedName("fk_id_rol") val fk_id_rol: Int? = null,
    @SerializedName("profile_photo_path") val profile_photo_path: String? = null
)

// Respuesta del login: mensaje, datos del usuario, nombre del rol y access_token que
// la app guarda en SessionManager para autenticar el resto de las llamadas.
data class LoginResponse(
    val message: String? = null,
    val user: User? = null,
    val role: String? = null,
    @SerializedName("access_token") val access_token: String? = null,
    @SerializedName("token_type") val token_type: String? = null
)

// Respuesta del cierre de sesión: solo confirmación del servidor.
data class LogoutResponse(
    val message: String? = null
)

// Catálogo de roles: identificador y nombre (admin, Instructor, Aprendiz). Permite
// decidir la navegación y los permisos de cada pantalla según el rol del usuario.
data class Role(
    @SerializedName("id_rol") val id_rol: Int? = null,
    @SerializedName("rol_name") val rol_name: String? = null
)

// Usuario completo como lo devuelve la API en los listados administrativos. Incluye
// el objeto Role anidado y utilidades para mostrar el nombre y comparar roles.
data class UsuarioApi(
    @SerializedName("id_usuario") val id_usuario: Int? = null,
    @SerializedName("user_identification") val user_identification: String? = null,
    @SerializedName("user_name") val user_name: String? = null,
    @SerializedName("user_lastname") val user_lastname: String? = null,
    @SerializedName("user_email") val user_email: String? = null,
    @SerializedName("user_coursenumber") val user_coursenumber: Int? = null,
    @SerializedName("user_program") val user_program: String? = null,
    @SerializedName("fk_id_rol") val fk_id_rol: Int? = null,
    @SerializedName("profile_photo_path") val profile_photo_path: String? = null,
    val role: Role? = null
) {
    val nombreCompleto: String get() = listOfNotNull(user_name, user_lastname).joinToString(" ").ifBlank { "Sin nombre" }
    fun esRol(rol: String): Boolean = role?.rol_name.equals(rol, ignoreCase = true)
}

// Registro de entrada de una persona al centro: fecha/hora, lugar y tipo de ingreso,
// junto con el usuario al que pertenece.
data class Ingreso(
    @SerializedName("id_ingreso") val id_ingreso: Int? = null,
    @SerializedName("ingreso_datetime") val ingreso_datetime: String? = null,
    @SerializedName("ingreso_place") val ingreso_place: String? = null,
    @SerializedName("ingreso_type") val ingreso_type: String? = null,
    @SerializedName("fk_id_user") val fk_id_user: Int? = null,
    val user: UsuarioApi? = null
)

// Registro de entrada de un equipo tecnológico: datos del dispositivo (tipo, marca,
// modelo, color, serial), observaciones, accesorios que lleva y el usuario que lo registró.
data class IngresoEquipo(
    @SerializedName("id_ingreso_equipo") val id_ingreso_equipo: Int? = null,
    @SerializedName("fk_id_usuario") val fk_id_usuario: Int? = null,
    @SerializedName("equipo_type") val equipo_type: String? = null,
    @SerializedName("equipo_brand") val equipo_brand: String? = null,
    @SerializedName("equipo_model") val equipo_model: String? = null,
    @SerializedName("equipo_color") val equipo_color: String? = null,
    @SerializedName("equipo_serial") val equipo_serial: String? = null,
    @SerializedName("equipo_observations") val equipo_observations: String? = null,
    @SerializedName("equipo_accesorios") val equipo_accesorios: List<Accesorio>? = null,
    @SerializedName("entry_datetime") val entry_datetime: String? = null,
    val user: UsuarioApi? = null
) {
    val marcaModelo: String get() = listOfNotNull(equipo_brand, equipo_model).joinToString(" ").ifBlank { "—" }
}

// Accesorio de un equipo registrado: tipo (Mouse, Teclado, Audífonos), marca, color y
// si es inalámbrico (aplica para mouse y audífonos).
data class Accesorio(
    @SerializedName("tipo") val tipo: String,
    @SerializedName("marca") val marca: String? = null,
    @SerializedName("color") val color: String? = null,
    @SerializedName("inalambrico") val inalambrico: Boolean? = null
)

// Cuerpo de la petición para registrar un equipo: datos del portátil más los
// accesorios que lleva. El backend asigna el usuario a partir del token.
data class IngresoEquipoRequest(
    @SerializedName("equipo_type") val equipo_type: String,
    @SerializedName("equipo_brand") val equipo_brand: String,
    @SerializedName("equipo_color") val equipo_color: String,
    @SerializedName("equipo_serial") val equipo_serial: String,
    @SerializedName("equipo_observations") val equipo_observations: String? = null,
    @SerializedName("equipo_accesorios") val equipo_accesorios: List<Accesorio>? = null
)

// Respuesta de la creación de un equipo: mensaje de confirmación y el registro creado.
data class EquipmentResponse(
    val message: String? = null,
    val data: IngresoEquipo? = null
)

// Novedad o aviso publicado por un instructor/admin: ambiente, título, cuerpo y fecha
// de publicación, con el autor asociado.
data class Novedad(
    @SerializedName("id_novedad") val id_novedad: Int? = null,
    @SerializedName("novedad_ambiente") val novedad_ambiente: String? = null,
    @SerializedName("novedad_title") val novedad_title: String? = null,
    @SerializedName("novedad_body") val novedad_body: String? = null,
    @SerializedName("novedad_datetime") val novedad_datetime: String? = null,
    @SerializedName("fk_id_usuario") val fk_id_usuario: Int? = null,
    val user: UsuarioApi? = null
)