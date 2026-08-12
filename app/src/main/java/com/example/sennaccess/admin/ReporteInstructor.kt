package com.example.sennaccess.admin

// Formulario de REPORTE AL INSTRUCTOR del ADMINISTRADOR (pestaña REPORTES).
// El campo "Usuario" se llena con instructores reales desde GET /admin/users
// (filtrados por rol Instructor); el resto del formulario sigue siendo solo vista.

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sennaccess.data.UsuarioApi
import com.example.sennaccess.ui.CargaUiState
import com.example.sennaccess.ui.EstadoContenido
import com.example.sennaccess.ui.theme.LocalAppColors
import com.example.sennaccess.ui.theme.SenaGreen
import com.example.sennaccess.ui.ios.GlassCornerRadius
import com.example.sennaccess.ui.ios.IosCollapsibleHeader
import com.example.sennaccess.ui.ios.glassSurface
import com.example.sennaccess.ui.ios.pressScale

/**
 * Formulario de reporte al instructor del ADMINISTRADOR (contenido de pestaña).
 * El dropdown de instructores consume GET /admin/users a traves del AdminDashboardViewModel.
 */
@Composable
fun ReporteInstructorContent(
    instructores: CargaUiState<List<UsuarioApi>>,
    onReintentar: () -> Unit,
    onNavigate: (AdminScreen) -> Unit
) {
    val colors = LocalAppColors.current
    // Estado local de los campos del formulario (fecha, hora, asunto, descripcion).
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var instructorSeleccionado by remember { mutableStateOf<UsuarioApi?>(null) }
    var dropdownAbierto by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Encabezado de la pantalla de reporte.
        IosCollapsibleHeader(
            title = "Reporte al Instructor",
            subtitle = "Comunicar una novedad o solicitud",
            scrollOffset = scrollState.value.toFloat()
        )

        Spacer(modifier = Modifier.height(12.dp))

        AdminGlassContainer {
            // Dropdown de instructores alimentado por GET /admin/users.
            EstadoContenido(estado = instructores, onReintentar = onReintentar) { listaUsuarios ->
                val soloInstructores = listaUsuarios.filter { it.esRol("Instructor") }

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = instructorSeleccionado?.nombreCompleto ?: "Seleccionar instructor",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Usuario") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = campoRepInsColors(),
                        trailingIcon = {
                            IconButton(onClick = { dropdownAbierto = true }) {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = SenaGreen)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = dropdownAbierto,
                        onDismissRequest = { dropdownAbierto = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        soloInstructores.forEach { instructor ->
                            DropdownMenuItem(
                                text = { Text(instructor.nombreCompleto, color = colors.textPrimary) },
                                onClick = {
                                    instructorSeleccionado = instructor
                                    dropdownAbierto = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            // Fecha y hora del reporte en fila de dos columnas.
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = fecha, onValueChange = { fecha = it },
                    label = { Text("Fecha") },
                    modifier = Modifier.weight(1f),
                    colors = campoRepInsColors()
                )
                OutlinedTextField(
                    value = hora, onValueChange = { hora = it },
                    label = { Text("Hora") },
                    modifier = Modifier.weight(1f),
                    colors = campoRepInsColors()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Asunto del reporte.
            OutlinedTextField(
                value = asunto, onValueChange = { asunto = it },
                label = { Text("Asunto") },
                modifier = Modifier.fillMaxWidth(),
                colors = campoRepInsColors()
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Descripcion ampliada del reporte.
            OutlinedTextField(
                value = descripcion, onValueChange = { descripcion = it },
                label = { Text("Descripcion") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                colors = campoRepInsColors()
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Boton Enviar: por ahora vuelve al panel (registro del reporte).
            Button(
                onClick = { onNavigate(AdminScreen.PANEL) },
                modifier = Modifier.fillMaxWidth().height(50.dp).pressScale(pressedScale = 0.97f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SenaGreen, contentColor = colors.textPrimary)
            ) { Text("Enviar", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

// Paleta de colores comun para los campos de este formulario.
@Composable
private fun campoRepInsColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = SenaGreen, unfocusedBorderColor = LocalAppColors.current.textSecondary,
    focusedLabelColor = SenaGreen, unfocusedLabelColor = LocalAppColors.current.textSecondary,
    cursorColor = SenaGreen, focusedTextColor = LocalAppColors.current.textPrimary, unfocusedTextColor = LocalAppColors.current.textPrimary
)
