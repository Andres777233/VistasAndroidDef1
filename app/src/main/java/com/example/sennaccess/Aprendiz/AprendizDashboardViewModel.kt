package com.example.sennaccess.aprendiz

// ViewModel del dashboard del Aprendiz: mantiene 5 flujos de estado CargaUiState
// (resumen, historial, comprobantes, novedades y perfil) alimentados desde la API
// vía los repositorios, con respaldo a datos de ejemplo cuando no hay sesión activa.

import androidx.lifecycle.ViewModel
import com.example.sennaccess.data.EquipoRepository
import com.example.sennaccess.data.Ingreso
import com.example.sennaccess.data.IngresoEquipo
import com.example.sennaccess.data.IngresoRepository
import com.example.sennaccess.data.Novedad
import com.example.sennaccess.data.NovedadRepository
import com.example.sennaccess.data.SessionManager
import com.example.sennaccess.data.UsuarioApi
import com.example.sennaccess.data.UsuarioRepository
import com.example.sennaccess.data.mock.MockData
import com.example.sennaccess.ui.CargaUiState
import com.example.sennaccess.ui.cargarConFallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Modelo del resumen: nombre del aprendiz y conteos derivados de sus registros.
data class ResumenAprendiz(
    val nombre: String,
    val ingresosCount: Int,
    val equiposCount: Int
)

class AprendizDashboardViewModel : ViewModel() {

    // Repositorios que encapsulan las llamadas Retrofit a los endpoints de la API.
    private val ingresoRepo = IngresoRepository()
    private val equipoRepo = EquipoRepository()
    private val usuarioRepo = UsuarioRepository()
    private val novedadRepo = NovedadRepository()

    // Patrón CargaUiState: cada flujo arranca en Loading y muta a Success o Error.
    // Se expone inmutable (asStateFlow) para que la UI solo observe.
    private val _resumen = MutableStateFlow<CargaUiState<ResumenAprendiz>>(CargaUiState.Loading)
    val resumen: StateFlow<CargaUiState<ResumenAprendiz>> = _resumen.asStateFlow()

    private val _historial = MutableStateFlow<CargaUiState<List<Ingreso>>>(CargaUiState.Loading)
    val historial: StateFlow<CargaUiState<List<Ingreso>>> = _historial.asStateFlow()

    private val _comprobantes = MutableStateFlow<CargaUiState<List<IngresoEquipo>>>(CargaUiState.Loading)
    val comprobantes: StateFlow<CargaUiState<List<IngresoEquipo>>> = _comprobantes.asStateFlow()

    private val _perfil = MutableStateFlow<CargaUiState<UsuarioApi>>(CargaUiState.Loading)
    val perfil: StateFlow<CargaUiState<UsuarioApi>> = _perfil.asStateFlow()

    private val _novedades = MutableStateFlow<CargaUiState<List<Novedad>>>(CargaUiState.Loading)
    val novedades: StateFlow<CargaUiState<List<Novedad>>> = _novedades.asStateFlow()

    // Carga todas las secciones al crear el ViewModel; el reintento las refresca.
    init {
        cargarResumen()
        cargarHistorial()
        cargarComprobantes()
        cargarPerfil()
        cargarNovedades()
    }

    // Calcula el resumen combinando dos llamadas (ingresos y equipos) del aprendiz.
    // cargarConFallback lanza una corrutina en viewModelScope: sin sesión usa el
    // fallback y con sesión marca Loading, llama a la API y muta a Success/Error.
    fun cargarResumen() {
        // Datos de ejemplo como respaldo cuando no hay sesión activa.
        val mock = ResumenAprendiz(
            nombre = SessionManager.userName ?: MockData.aprendizDemo.nombreCompleto,
            ingresosCount = MockData.historialAprendiz.size,
            equiposCount = MockData.equipos.size
        )
        cargarConFallback(fallback = { mock }, setState = { _resumen.value = it }) {
            val ingresos = ingresoRepo.getMyIngresos(SessionManager.token!!)
            val equipos = equipoRepo.getMyEquipment(SessionManager.token!!)
            ResumenAprendiz(
                nombre = SessionManager.userName ?: MockData.aprendizDemo.nombreCompleto,
                ingresosCount = ingresos.size,
                equiposCount = equipos.size
            )
        }
    }

    // Historial de ingresos del aprendiz, con la misma lógica de fallback a mocks.
    fun cargarHistorial() {
        cargarConFallback(fallback = { MockData.historialAprendiz }, setState = { _historial.value = it }) {
            ingresoRepo.getMyIngresos(SessionManager.token!!)
        }
    }

    // Comprobantes de equipos registrados por el aprendiz vía EquipoRepository.
    fun cargarComprobantes() {
        cargarConFallback(fallback = { MockData.equipos }, setState = { _comprobantes.value = it }) {
            equipoRepo.getMyEquipment(SessionManager.token!!)
        }
    }

    // Perfil del aprendiz logueado, consumido desde UsuarioRepository.
    fun cargarPerfil() {
        cargarConFallback(fallback = { MockData.aprendizDemo }, setState = { _perfil.value = it }) {
            usuarioRepo.getCurrentUser(SessionManager.token!!)
        }
    }

    // Novedades del centro vía NovedadRepository; después de abrir el backend
    // al aprendiz consume GET /novedades en lugar de mostrar solo mocks.
    fun cargarNovedades() {
        cargarConFallback(fallback = { MockData.novedades }, setState = { _novedades.value = it }) {
            novedadRepo.getNovedades(SessionManager.token!!)
        }
    }
}
