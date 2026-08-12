package com.example.sennaccess.admin

// Contenido de la pestaña HISTORIAL del ADMINISTRADOR.
// Muestra el registro de ingresos al centro agrupado en dos secciones
// (instructores y aprendices). Los datos llegan desde el backend vía ViewModel.

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sennaccess.ui.CargaUiState
import com.example.sennaccess.ui.EstadoContenido
import com.example.sennaccess.ui.theme.LocalAppColors
import com.example.sennaccess.ui.theme.SenaGreen
import com.example.sennaccess.ui.ios.GlassCornerRadius
import com.example.sennaccess.ui.ios.IosCollapsibleHeader
import com.example.sennaccess.ui.ios.glassSurface

/**
 * Historial de acceso del ADMINISTRADOR (contenido de la pestaña HISTORIAL).
 *
 * Dos secciones separadas en contenedores propios: INSTRUCTORES y APRENDICES,
 * agrupadas por el rol del usuario que registró cada ingreso.
 */
@Composable
fun HistorialAdminContent(
    historial: CargaUiState<HistorialAdminData>,
    onReintentar: () -> Unit,
    onVerAprendices: () -> Unit = {},
    onVerInstructores: () -> Unit = {}
) {
    // Estado de desplazamiento para el encabezado colapsable.
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Encabezado de la pantalla de historial.
        IosCollapsibleHeader(
            title = "Historial de Acceso",
            subtitle = "Registro de ingresos al centro",
            scrollOffset = scrollState.value.toFloat()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // EstadoContenido gestiona carga/error/éxito del historial.
        EstadoContenido(estado = historial, onReintentar = onReintentar) { data ->
            // Sección de accesos de instructores.
            SeccionHistorial(
                titulo = "INSTRUCTORES",
                icono = Icons.Default.School,
                registros = data.instructores
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botones para ver listados filtrados de aprendices o instructores.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onVerAprendices,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Ver Aprendices", color = SenaGreen, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = onVerInstructores,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Ver Instructores", color = SenaGreen, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// Sección reutilizable: encabezado con ícono y lista de registros de un rol.
@Composable
private fun SeccionHistorial(
    titulo: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    registros: List<RegistroAccesoAdmin>
) {
    val colors = LocalAppColors.current
    AdminGlassContainer {
        // Encabezado de la sección (ícono + título del rol).
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icono, contentDescription = null, tint = SenaGreen, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(titulo, color = SenaGreen, fontWeight = FontWeight.Bold, fontSize = 15.sp, letterSpacing = 1.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (registros.isEmpty()) {
            // Sin registros para este rol.
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                Text("Sin registros", color = colors.textSecondary, fontSize = 13.sp)
            }
        } else {
            // Lista los registros de acceso como tarjetas individuales.
            registros.forEach { registro ->
                TarjetaAccesoAdmin(
                    nombre = registro.nombre,
                    rol = registro.rol,
                    hora = registro.hora,
                    tipo = registro.tipo
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

// Tarjeta de un acceso: nombre, rol, hora y etiqueta de entrada o salida.
@Composable
internal fun TarjetaAccesoAdmin(nombre: String, rol: String, hora: String, tipo: String) {
    val colors = LocalAppColors.current
    val esSalida = tipo.equals("Salida", ignoreCase = true)
    val colorTipo = if (esSalida) Color(0xFFE67E22) else SenaGreen
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .glassSurface(cornerRadius = GlassCornerRadius)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar circular con reloj que identifica un acceso.
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(colorTipo.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Schedule, null, tint = colorTipo, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        // Nombre y rol del usuario a la izquierda; hora y tipo resaltados a la derecha.
        Column(modifier = Modifier.weight(1f)) {
            Text(nombre, color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(rol, color = colors.textSecondary, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if (esSalida) "SALIDA" else "ENTRADA",
                color = colorTipo,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(hora, color = colors.textSecondary, fontSize = 12.sp)
        }
    }
}
