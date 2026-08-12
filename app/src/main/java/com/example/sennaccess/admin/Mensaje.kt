package com.example.sennaccess.admin

// Pantalla de MENSAJE/CONFIRMACIÓN del ADMINISTRADOR.
// Muestra un resultado al usuario (éxito o advertencia) con un botón único
// que regresa al panel de inicio.

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sennaccess.ui.theme.LocalAppColors
import com.example.sennaccess.ui.theme.OrangeAmber
import com.example.sennaccess.ui.theme.SenaGreen
import com.example.sennaccess.ui.ios.GlassCornerRadius
import com.example.sennaccess.ui.ios.glassSurface
import com.example.sennaccess.ui.ios.pressScale

/**
 * Pantalla de confirmación/advertencia del ADMINISTRADOR (contenido de pestaña).
 */
@Composable
fun MensajeContent(
    titulo: String,
    descripcion: String,
    tipo: String,
    onVolver: () -> Unit
) {
    val colors = LocalAppColors.current
    // Indica si el mensaje es una advertencia (ajusta ícono, color y borde).
    val esAdvertencia = tipo == "advertencia"

    // Capa centrada a pantalla completa que enfoca el mensaje.
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Tarjeta de vidrio cuyo borde e ícono dependen del tipo de mensaje.
        Column(
            modifier = Modifier
                .padding(32.dp)
                .glassSurface(cornerRadius = GlassCornerRadius)
                .border(
                    1.dp,
                    if (esAdvertencia) OrangeAmber.copy(alpha = 0.4f) else Color.Transparent,
                    RoundedCornerShape(GlassCornerRadius)
                )
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ícono de advertencia (ámbar) o de éxito (verde) según el tipo.
            Icon(
                imageVector = if (esAdvertencia) Icons.Default.Warning else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (esAdvertencia) OrangeAmber else SenaGreen,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Título y descripción del mensaje para el usuario.
            Text(titulo, color = colors.textPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(12.dp))
            Text(descripcion, color = colors.textSecondary, fontSize = 15.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(32.dp))
            // Botón único que devuelve al panel principal.
            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth().height(50.dp).pressScale(pressedScale = 0.97f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SenaGreen, contentColor = colors.textPrimary)
            ) { Text("Volver al panel", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
        }
    }
}
