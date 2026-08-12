package com.example.sennaccess.ui

// Vista de novedades compartida por Aprendiz e Instructor: lista las novedades
// del centro y permite reportar una nueva mediante un formulario local.

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sennaccess.data.Novedad
import com.example.sennaccess.data.mock.MockData
import com.example.sennaccess.ui.ios.GlassCornerRadius
import com.example.sennaccess.ui.ios.IosCollapsibleHeader
import com.example.sennaccess.ui.ios.glassSurface
import com.example.sennaccess.ui.ios.pressScale
import com.example.sennaccess.ui.theme.LocalAppColors
import com.example.sennaccess.ui.theme.OrangeAmber
import com.example.sennaccess.ui.theme.SenaGreen

/**
 * Vista de Novedades compartida para APRENDIZ e INSTRUCTOR.
 * Muestra el listado de novedades del centro y permite reportar una nueva.
 *
 * - [estado] == null → solo datos de ejemplo (rol Aprendiz, la API no expone novedades).
 * - [estado] != null → carga desde la API (rol Instructor/Admin) con respaldo a mocks.
 * Respeta colores SENA y estilo glassmorphism iOS.
 */
@Composable
fun NovedadesView(
    estado: CargaUiState<List<Novedad>>? = null,
    onReintentar: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    val scrollState = rememberScrollState()

    // Estado local del flujo de reporte: visibilidad del formulario, textos y envío.
    var mostrandoFormulario by remember { mutableStateOf(false) }
    var titulo by remember { mutableStateOf("") }
    var detalle by remember { mutableStateOf("") }
    var enviada by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        IosCollapsibleHeader(
            title = "Novedades",
            subtitle = "Avisos y reportes del centro de formación",
            scrollOffset = scrollState.value.toFloat()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // El área superior alterna entre confirmación de envío, formulario y botón.
        if (enviada) {
            TarjetaNovedadEnviada(onAceptar = { enviada = false })
        } else if (mostrandoFormulario) {
            FormularioNovedad(
                titulo = titulo,
                detalle = detalle,
                onTituloChange = { titulo = it },
                onDetalleChange = { detalle = it },
                onEnviar = { enviada = true; mostrandoFormulario = false },
                onCancelar = { mostrandoFormulario = false }
            )
        } else {
            Button(
                onClick = { mostrandoFormulario = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .pressScale(pressedScale = 0.97f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SenaGreen, contentColor = Color.Black)
            ) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("REPORTAR NOVEDAD", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "NOVEDADES RECIENTES",
            color = colors.textSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Sin estado se pintan novedades de ejemplo; con estado se consume la API
        // a través de EstadoContenido (Loading/Error/Success con reintento).
        if (estado == null) {
            MockData.novedades.forEach { n ->
                TarjetaNovedad(n)
                Spacer(modifier = Modifier.height(12.dp))
            }
        } else {
            EstadoContenido(estado = estado, onReintentar = onReintentar) { items ->
                if (items.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                        Text("No hay novedades registradas.", color = colors.textSecondary, fontSize = 14.sp)
                    }
                } else {
                    items.forEach { n ->
                        TarjetaNovedad(n)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

// Tarjeta glassmorphism que muestra una novedad con su detalle y estado pendiente.
@Composable
private fun TarjetaNovedad(n: Novedad) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pressScale(pressedScale = 0.98f)
            .glassSurface(cornerRadius = GlassCornerRadius)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(OrangeAmber.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.ReportProblem, contentDescription = null, tint = OrangeAmber, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(n.novedad_title ?: "Novedad", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(n.novedad_datetime ?: "—", color = colors.textSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(n.novedad_body ?: "—", color = colors.textSecondary, fontSize = 12.sp, lineHeight = 16.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .border(1.dp, OrangeAmber.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text("PENDIENTE", color = OrangeAmber, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// Formulario de reporte con campos de título y descripción y acciones Enviar/Cancelar.
@Composable
private fun FormularioNovedad(
    titulo: String,
    detalle: String,
    onTituloChange: (String) -> Unit,
    onDetalleChange: (String) -> Unit,
    onEnviar: () -> Unit,
    onCancelar: () -> Unit
) {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .glassSurface(cornerRadius = GlassCornerRadius)
            .padding(18.dp)
    ) {
        Text("Reportar Novedad", color = colors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(14.dp))
        OutlinedTextField(
            value = titulo,
            onValueChange = onTituloChange,
            label = { Text("Titulo de la novedad") },
            modifier = Modifier.fillMaxWidth(),
            colors = novedadCamposColors()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = detalle,
            onValueChange = onDetalleChange,
            label = { Text("Descripcion") },
            modifier = Modifier.fillMaxWidth().height(110.dp),
            colors = novedadCamposColors()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onEnviar,
                modifier = Modifier.weight(1f).height(48.dp).pressScale(pressedScale = 0.97f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SenaGreen, contentColor = Color.Black)
            ) { Text("ENVIAR", fontWeight = FontWeight.Bold) }
            OutlinedButton(
                onClick = onCancelar,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, colors.textSecondary)
            ) { Text("CANCELAR", color = colors.textPrimary) }
        }
    }
}

// Confirmación visual que se muestra tras registrar una novedad.
@Composable
private fun TarjetaNovedadEnviada(onAceptar: () -> Unit) {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .glassSurface(cornerRadius = GlassCornerRadius)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.WarningAmber, contentDescription = null, tint = SenaGreen, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Novedad Reportada", color = SenaGreen, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Tu reporte fue registrado y quedara visible para el centro de formacion.",
            color = colors.textSecondary,
            fontSize = 14.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onAceptar,
            modifier = Modifier.fillMaxWidth().height(48.dp).pressScale(pressedScale = 0.97f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SenaGreen, contentColor = Color.Black)
        ) { Text("ACEPTAR", fontWeight = FontWeight.Bold) }
    }
}

// Esquema de colores SENA para los campos del formulario.
@Composable
private fun novedadCamposColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = SenaGreen,
    unfocusedBorderColor = LocalAppColors.current.textSecondary.copy(alpha = 0.5f),
    focusedLabelColor = SenaGreen,
    unfocusedLabelColor = LocalAppColors.current.textSecondary,
    cursorColor = SenaGreen,
    focusedTextColor = LocalAppColors.current.textPrimary,
    unfocusedTextColor = LocalAppColors.current.textPrimary
)
