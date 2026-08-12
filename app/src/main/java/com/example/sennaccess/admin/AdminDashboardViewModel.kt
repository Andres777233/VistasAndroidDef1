package com.example.sennaccess.admin

// ViewModel del dashboard del ADMINISTRADOR.
// Obtiene del backend el resumen, el historial y el perfil, siempre bajo el
// patrón CargaUiState para que la UI distinga carga, error y datos listos,
// con respaldo a MockData cuando no hay sesión o la API falla.

import androidx.lifecycle.ViewModel
import com.example.sennaccess.data.Ingreso
import com.example.sennaccess.data.IngresoRepository
import com.example.sennaccess.data.Role
import com.example.sennaccess.data.SessionManager
import com.example.sennaccess.data.UsuarioApi
import com.example.sennaccess.data.UsuarioRepository
import com.example.sennaccess.data.mock.MockData
import com.example.sennaccess.ui.CargaUiState
import com.example.sennaccess.ui.cargarConFallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Modelo de un registro de acceso en el historial del admin (nombre, rol, hora y tipo).
data class RegistroAccesoAdmin(
    val nombre: String,
    val rol: String,
    val hora: String,
    val tipo: String
)

// Modelo del historial del admin: separa los accesos por rol (instructores y aprendices).
data class HistorialAdminData(
    val instructores: List<RegistroAccesoAdmin>,
    val aprendices: List<RegistroAccesoAdmin>
)

class AdminDashboardViewModel : ViewModel() {

    // Repositorios que encapsulan el acceso a la API (ingresos y usuarios).
    private val ingresoRepo = IngresoRepository()
    private val usuarioRepo = UsuarioRepository()

    // Estados de la UI como StateFlow: la pantalla observa estas propiedades
    // y reacciona ante Loading/Success/Error sin acoplar la lógica de red a Compose.
    private val _resumen = MutableStateFlow<CargaUiState<List<Ingreso>>>(CargaUiState.Loading)
    val resumen: StateFlow<CargaUiState<List<Ingreso>>> = _resumen.asStateFlow()

    private val _historial = MutableStateFlow<CargaUiState<HistorialAdminData>>(CargaUiState.Loading)
    val historial: StateFlow<CargaUiState<HistorialAdminData>> = _historial.asStateFlow()

    private val _perfil = MutableStateFlow<CargaUiState<UsuarioApi>>(CargaUiState.Loading)
    val perfil: StateFlow<CargaUiState<UsuarioApi>> = _perfil.asStateFlow()

    private val _roles = MutableStateFlow<CargaUiState<List<Role>>>(CargaUiState.Loading)
    val roles: StateFlow<CargaUiState<List<Role>>> = _roles.asStateFlow()

    private val _usuarios = MutableStateFlow<CargaUiState<List<UsuarioApi>>>(CargaUiState.Loading)
    val usuarios: StateFlow<CargaUiState<List<UsuarioApi>>> = _usuarios.asStateFlow()

    init {
        // Al crear el ViewModel se disparan las cargas iniciales en paralelo.
        cargarResumen()
        cargarHistorial()
        cargarPerfil()
        cargarRoles()
        cargarUsuarios()
    }

    // Carga el resumen de ingresos del día; si no hay sesión o la API falla,
    // se entregan los datos de ejemplo de MockData.
    fun cargarResumen() {
        cargarConFallback(fallback = { MockData.ingresos }, setState = { _resumen.value = it }) {
            ingresoRepo.getIngresos(SessionManager.token!!)
        }
    }

    // Carga ingresos y usuarios, cruza el rol de cada uno y construye el historial
    // separado por instructores/aprendices. Fallback a mock en modo demo o si falla.
    fun cargarHistorial() {
        cargarConFallback(fallback = { buildHistorial(MockData.ingresos, emptyMap()) }, setState = { _historial.value = it }) {
            val ingresos = ingresoRepo.getIngresos(SessionManager.token!!)
            val usuarios = usuarioRepo.getUsers(SessionManager.token!!)
            val rolesPorUsuario = usuarios.associate { it.id_usuario to it.role?.rol_name }
            buildHistorial(ingresos, rolesPorUsuario)
        }
    }

    // Carga el perfil del admin actual desde la API (o el demo de MockData).
    fun cargarPerfil() {
        cargarConFallback(fallback = { MockData.adminDemo }, setState = { _perfil.value = it }) {
            usuarioRepo.getCurrentUser(SessionManager.token!!)
        }
    }

    // Carga el listado completo de usuarios (GET /admin/users); el AdminDashboard
    // lo usa para filtrar instructores en el reporte al instructor.
    fun cargarUsuarios() {
        cargarConFallback(fallback = { MockData.usuarios }, setState = { _usuarios.value = it }) {
            usuarioRepo.getUsers(SessionManager.token!!)
        }
    }

    // Carga el catalogo de roles desde la API (GET /admin/roles); fallback a los
    // roles de ejemplo cuando no hay sesion activa.
    fun cargarRoles() {
        val mockRoles = listOf(MockData.rolAdmin, MockData.rolInstructor, MockData.rolAprendiz)
        cargarConFallback(fallback = { mockRoles }, setState = { _roles.value = it }) {
            usuarioRepo.getRoles(SessionManager.token!!)
        }
    }

    // Clasifica cada ingreso según el rol del usuario: prioriza el rol anidado
    // del ingreso y resuelve contra rolesPorUsuario cuando este viene nulo.
    private fun buildHistorial(
        ingresos: List<Ingreso>,
        rolesPorUsuario: Map<Int?, String?>
    ): HistorialAdminData {
        val instructores = mutableListOf<RegistroAccesoAdmin>()
        val aprendices = mutableListOf<RegistroAccesoAdmin>()
        ingresos.forEach { ingreso ->
            val rol = ingreso.user?.role?.rol_name ?: rolesPorUsuario[ingreso.fk_id_user]
            // Enruta el ingreso a la lista correspondiente según su rol.
            when {
                rol.equals("Instructor", ignoreCase = true) ->
                    instructores += RegistroAccesoAdmin(
                        nombre = ingreso.user?.nombreCompleto ?: "Usuario",
                        rol = "Instructor",
                        hora = ingreso.ingreso_datetime ?: "",
                        tipo = ingreso.ingreso_type ?: "Entrada"
                    )
                rol.equals("Aprendiz", ignoreCase = true) ->
                    aprendices += RegistroAccesoAdmin(
                        nombre = ingreso.user?.nombreCompleto ?: "Usuario",
                        rol = "Aprendiz",
                        hora = ingreso.ingreso_datetime ?: "",
                        tipo = ingreso.ingreso_type ?: "Entrada"
                    )
            }
        }
        return HistorialAdminData(instructores, aprendices)
    }
}
