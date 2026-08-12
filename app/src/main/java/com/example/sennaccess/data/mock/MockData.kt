package com.example.sennaccess.data.mock

import com.example.sennaccess.data.Ingreso
import com.example.sennaccess.data.IngresoEquipo
import com.example.sennaccess.data.Novedad
import com.example.sennaccess.data.Role
import com.example.sennaccess.data.UsuarioApi

// Conjunto de datos de ejemplo que sirve como respaldo de la app cuando no hay
// conexión real: alimenta las pantallas en modo demo y evita que queden vacías.

/**
 * Datos de ejemplo usados como respaldo cuando no hay sesión activa
 * (botones demo) o cuando la API falla, para que la app nunca quede vacía.
 */
object MockData {

    // Roles de referencia que se asignan a los usuarios de ejemplo.
    val rolAdmin = Role(id_rol = 1, rol_name = "admin")
    val rolInstructor = Role(id_rol = 2, rol_name = "Instructor")
    val rolAprendiz = Role(id_rol = 3, rol_name = "Aprendiz")

    // Usuarios ficticios que cubren los tres roles; se usan en listados y estadísticas.
    val usuarios = listOf(
        UsuarioApi(1, "1000000000", "admin", "System", "admin@sena.edu.co", 0, "Administration", 1, null, rolAdmin),
        UsuarioApi(2, "12345678", "Carlos", "Méndez", "carlos.mendez@sena.edu.co", 2876541, "ADSO", 2, null, rolInstructor),
        UsuarioApi(3, "87654321", "Daniel", "Vega", "daniel.vega@sena.edu.co", 2876542, "Análisis y Desarrollo de Software", 2, null, rolInstructor),
        UsuarioApi(4, "11223344", "María", "Gómez", "maria.gomez@sena.edu.co", 2876543, "Gestión Empresarial", 2, null, rolInstructor),
        UsuarioApi(5, "12345678", "Pepito", "Perez", "pepito@sena.edu.co", 2876541, "ADSO", 3, null, rolAprendiz),
        UsuarioApi(6, "87654321", "Laura", "Pérez", "laura.perez@sena.edu.co", 2876541, "ADSO", 3, null, rolAprendiz),
        UsuarioApi(7, "11223344", "Sofía", "Ramírez", "sofia.ramirez@sena.edu.co", 2876542, "Diseño Gráfico", 3, null, rolAprendiz)
    )

    // Filtros derivados: separan los usuarios de ejemplo según su rol.
    val instructores: List<UsuarioApi> get() = usuarios.filter { it.esRol("Instructor") }
    val aprendices: List<UsuarioApi> get() = usuarios.filter { it.esRol("Aprendiz") }

    // Accesos directos para el modo demo: cada rol tiene un usuario predeterminado.
    val aprendizDemo: UsuarioApi get() = aprendices.first()
    val instructorDemo: UsuarioApi get() = instructores.first()
    val adminDemo: UsuarioApi get() = usuarios.first()

    // Ingresos de ejemplo de la jornada, ligados a los usuarios ficticios.
    val ingresos = listOf(
        Ingreso(1, "27/5/2026, 8:45 a. m.", "CCyS", "Entrada", 2, instructores[0]),
        Ingreso(2, "27/5/2026, 8:30 a. m.", "CCyS", "Entrada", 6, aprendices[1]),
        Ingreso(3, "27/5/2026, 8:15 a. m.", "CCyS", "Entrada", 3, instructores[1]),
        Ingreso(4, "27/5/2026, 7:55 a. m.", "CCyS", "Entrada", 4, instructores[2]),
        Ingreso(5, "27/5/2026, 7:20 a. m.", "CCyS", "Entrada", 7, aprendices[2]),
        Ingreso(6, "27/5/2026, 6:58 a. m.", "CCyS", "Entrada", 5, aprendices[0])
    )

    // Historial repetido de un mismo aprendiz, para simular múltiples entradas.
    val historialAprendiz = listOf(
        Ingreso(10, "27/5/2026, 8:45:25 a. m.", "CCyS", "Entrada", 5, aprendices[0]),
        Ingreso(11, "27/5/2026, 8:45:19 a. m.", "CCyS", "Entrada", 5, aprendices[0]),
        Ingreso(12, "27/5/2026, 8:11:10 a. m.", "CCyS", "Entrada", 5, aprendices[0])
    )

    // Equipos de ejemplo registrados a nombre de un aprendiz.
    val equipos = listOf(
        IngresoEquipo(
            id_ingreso_equipo = 1, fk_id_usuario = 5, equipo_type = "Portátil",
            equipo_brand = "Lenovo", equipo_model = "ThinkPad", equipo_color = "Negro",
            equipo_serial = "SN12345", entry_datetime = "27/5/2026, 8:40 a. m.", user = aprendices[0]
        ),
        IngresoEquipo(
            id_ingreso_equipo = 2, fk_id_usuario = 5, equipo_type = "Tablet",
            equipo_brand = "Samsung", equipo_model = "Tab S9", equipo_color = "Gris",
            equipo_serial = "SN67890", equipo_observations = "Cargador incluido",
            entry_datetime = "27/5/2026, 8:41 a. m.", user = aprendices[0]
        )
    )

    // Novedades de ejemplo publicadas por instructores.
    val novedades = listOf(
        Novedad(1, "Ambiente 204", "Mantenimiento preventivo de equipos", "Se realizará mantenimiento al parque tecnológico del centro de formación.", "27/5/2026, 8:30 a. m.", 2, instructores[0]),
        Novedad(2, "Oficina coordinación", "Cambio de jornada instructores", "Instructores con jornada tarde deben confirmar el nuevo horario con el coordinador.", "26/5/2026, 2:00 p. m.", 3, instructores[1])
    )
}
