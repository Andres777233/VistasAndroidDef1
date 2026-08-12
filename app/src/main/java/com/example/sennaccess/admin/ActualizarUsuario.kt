package com.example.sennaccess.admin

// Formulario para actualizar los datos de un usuario existente (rol ADMIN).
// Recibe el objeto UsuarioApi obtenido desde GET /admin/users y prellena
// los campos editables; al enviar muestra confirmación y navega de vuelta.

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sennaccess.data.UsuarioApi
import com.example.sennaccess.ui.theme.LocalAppColors
import com.example.sennaccess.ui.theme.SenaGreen
import com.example.sennaccess.ui.ios.GlassCornerRadius
import com.example.sennaccess.ui.ios.IosCollapsibleHeader
import com.example.sennaccess.ui.ios.glassSurface
import com.example.sennaccess.ui.ios.pressScale

/**
 * Formulario para actualizar usuario del ADMINISTRADOR (sub-pantalla).
 * Prellena los campos con los datos reales del GET /admin/users.
 */
@Composable
fun ActualizarUsuarioContent(
    usuario: UsuarioApi,
    onNavigate: (AdminScreen) -> Unit
) {
    // Estado local de los campos editables, inicializados desde el usuario del GET.
    var correo by remember { mutableStateOf(usuario.user_email ?: "") }
    var tipoId by remember { mutableStateOf("CC") }
    var numeroId by remember { mutableStateOf(usuario.user_identification ?: "") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var actualizado by remember { mutableStateOf(false) }
    val colors = LocalAppColors.current
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Encabezado de la pantalla de edicion.
            IosCollapsibleHeader(
                title = "Actualizar Usuario",
                subtitle = "Edicion de datos del usuario",
                scrollOffset = scrollState.value.toFloat()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contenedor de vidrio; encabeza con el nombre del usuario en edicion.
            AdminGlassContainer {
                Text(
                    "Actualizar Datos De ${usuario.nombreCompleto}",
                    color = colors.textPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                // Dos columnas de campos: izquierda (correo, tipo/numero de ID)
                // y derecha (direccion, telefono, PIN).
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo Electronico") }, modifier = Modifier.fillMaxWidth(), colors = campoColors())
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(value = tipoId, onValueChange = { tipoId = it }, label = { Text("Tipo De Identificacion") }, modifier = Modifier.fillMaxWidth(), colors = campoColors())
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(value = numeroId, onValueChange = { numeroId = it }, label = { Text("Numero De Identificacion") }, modifier = Modifier.fillMaxWidth(), colors = campoColors())
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Direccion") }, modifier = Modifier.fillMaxWidth(), colors = campoColors())
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Telefono") }, modifier = Modifier.fillMaxWidth(), colors = campoColors())
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(value = pin, onValueChange = { pin = it }, label = { Text("PIN") }, modifier = Modifier.fillMaxWidth(), colors = campoColors())
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                // Acciones: cancelar (vuelve al panel) y actualizar (muestra confirmacion).
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { onNavigate(AdminScreen.PANEL) },
                        modifier = Modifier.weight(1f).height(50.dp).pressScale(pressedScale = 0.97f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, colors.textSecondary)
                    ) { Text("CANCELAR", color = colors.textSecondary, fontWeight = FontWeight.Bold) }
                    Button(
                        onClick = { actualizado = true },
                        modifier = Modifier.weight(1f).height(50.dp).pressScale(pressedScale = 0.97f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SenaGreen, contentColor = colors.textPrimary)
                    ) { Text("ACTUALIZAR", fontWeight = FontWeight.Bold) }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Tras actualizar, overlay de exito sobre el formulario.
        if (actualizado) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .glassSurface(cornerRadius = GlassCornerRadius)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = SenaGreen, modifier = Modifier.size(80.dp))
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("¡Actualizacion Exitosa!", color = SenaGreen, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Los datos de ${usuario.nombreCompleto} han sido actualizados correctamente.",
                        color = colors.textPrimary, fontSize = 18.sp, textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { onNavigate(AdminScreen.PANEL) },
                        modifier = Modifier.fillMaxWidth().height(50.dp).pressScale(pressedScale = 0.97f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SenaGreen, contentColor = colors.textPrimary)
                    ) { Text("Volver al panel", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                }
            }
        }
    }
}

// Paleta de colores comun de los campos del formulario de actualizacion.
@Composable
private fun campoColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = SenaGreen, unfocusedBorderColor = LocalAppColors.current.textSecondary,
    focusedLabelColor = SenaGreen, unfocusedLabelColor = LocalAppColors.current.textSecondary,
    cursorColor = SenaGreen, focusedTextColor = LocalAppColors.current.textPrimary, unfocusedTextColor = LocalAppColors.current.textPrimary
)
