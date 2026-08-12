package com.example.sennaccess.ui.theme

// Pantalla de acceso biométrico: verifica dos huellas (índice izquierdo y derecho)
// mediante una simulación de escaneo. Habilita el botón de continuar solo cuando
// ambas huellas fueron capturadas; lleva a la pantalla de carga o regresa al login.

// Importaciones necesarias para el diseño, modificadores, iconos y estado
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.sennaccess.ui.theme.LocalAppColors
import com.example.sennaccess.ui.ios.GlowSpheres
import com.example.sennaccess.ui.ios.glassSurface
import com.example.sennaccess.ui.ios.GlassCornerRadiusLg
import com.example.sennaccess.ui.ios.pressScale
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun FingerprintScreen(onBackToLogin: () -> Unit, onNavigateToLoading: () -> Unit, isDark: Boolean = true, onToggleTheme: () -> Unit = {}) {
    // Composable principal: coordina el estado de captura de las dos huellas.
    val colors = LocalAppColors.current

    // --- MANEJO DE ESTADO ---
    // Esto es el equivalente directo a 'useState' en React.
    // 'remember' le dice a Compose que guarde el valor aunque la pantalla se vuelva a dibujar.
    var leftCaptured by remember { mutableStateOf(false) }
    var rightCaptured by remember { mutableStateOf(false) }
    var scanningLeft by remember { mutableStateOf(false) }
    var scanningRight by remember { mutableStateOf(false) }

    // --- CORRUTINAS ---
    // Las corrutinas son la forma en que Android maneja procesos en segundo plano.
    // Lo necesitamos para reemplazar el 'setTimeout' y hacer pausas sin congelar la app.
    val coroutineScope = rememberCoroutineScope()

    // Variable derivada: Solo es true si ambas huellas ya fueron capturadas.
    val allCaptured = leftCaptured && rightCaptured

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        IconButton(
            onClick = onToggleTheme,
            modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)
        ) {
            Icon(
                imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = if (isDark) "Modo claro" else "Modo oscuro",
                tint = colors.textPrimary
            )
        }

        // --- PATRÓN DE FONDO ---
        Image(
            painter = rememberAsyncImagePainter("https://www.sena.edu.co/Style%20Library/alayout/images/pattern.png"),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.12f), // Opacidad del 12%
            contentScale = ContentScale.Crop
        )

        // Luces ambientales detrás del vidrio
        GlowSpheres(isDark = isDark)

        // Contenedor principal centrado
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // --- TARJETA PRINCIPAL VIDRIO ESMERILADO iOS ---
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .glassSurface(cornerRadius = GlassCornerRadiusLg, elevated = true)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                // Columna con scroll para que no se corte en pantallas pequeñas
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- ENCABEZADO ---
                    Icon(imageVector = Icons.Default.Fingerprint, contentDescription = null, tint = SenaGreen, modifier = Modifier.size(64.dp).padding(bottom = 8.dp))

                    // Texto con dos colores diferentes usando buildAnnotatedString
                    Text(
                        text = buildAnnotatedString {
                            append("Acceso con ")
                            withStyle(style = SpanStyle(color = SenaGreen)) { append("Huella") }
                        },
                        fontSize = 28.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary
                    )
                    Text("Verificación biométrica para ingreso", fontSize = 14.sp, color = ThemeText.copy(alpha = 0.7f), textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp, bottom = 24.dp))

                    // Línea divisoria sutil
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(0.4f).padding(bottom = 24.dp), color = SenaGreen.copy(alpha = 0.3f))

                    // --- COMPONENTE: ÍNDICE IZQUIERDO ---
                    // Llamamos a nuestro componente reutilizable y le pasamos su estado
                    FingerprintCaptureCard(
                        title = "Índice Izquierdo",
                        isScanning = scanningLeft,
                        isCaptured = leftCaptured,
                        onScanClick = {
                            // Al hacer clic, iniciamos la corrutina (hilo en segundo plano)
                            coroutineScope.launch {
                                scanningLeft = true // Inicia la animación de escaneo
                                delay(2500) // Pausa de 2.5 segundos (equivalente a setTimeout)
                                scanningLeft = false // Detiene la animación
                                leftCaptured = true // Marca como verificado
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- COMPONENTE: ÍNDICE DERECHO ---
                    FingerprintCaptureCard(
                        title = "Índice Derecho",
                        isScanning = scanningRight,
                        isCaptured = rightCaptured,
                        onScanClick = {
                            coroutineScope.launch {
                                scanningRight = true
                                delay(2500)
                                scanningRight = false
                                rightCaptured = true
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- BOTÓN FINALIZAR ---
                    Button(
                        onClick = onNavigateToLoading,
                        enabled = allCaptured, // Solo se habilita si ambas variables son true
                        modifier = Modifier.fillMaxWidth().height(56.dp).pressScale(pressedScale = 0.97f).shadow(if (allCaptured) 15.dp else 0.dp, RoundedCornerShape(12.dp), spotColor = SenaGreen),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SenaGreen, contentColor = colors.textOnPrimary,
                            // Colores para cuando el botón está deshabilitado
                            disabledContainerColor = colors.divider, disabledContentColor = colors.textSecondary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (allCaptured) {
                                Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("FINALIZAR Y CONTINUAR", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                            } else {
                                Text("COMPLETE AMBAS VERIFICACIONES", fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- BOTÓN VOLVER ---
                    TextButton(onClick = onBackToLogin) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = ThemeText.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Volver al inicio de sesión", color = ThemeText.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// --- COMPONENTE REUTILIZABLE (Equivalente a un Functional Component en React) ---
// Extraemos la tarjeta de huella para no escribir el mismo código dos veces.
// Recibe parámetros (props) para saber qué título mostrar y en qué estado está.
@Composable
fun FingerprintCaptureCard(title: String, isScanning: Boolean, isCaptured: Boolean, onScanClick: () -> Unit) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            // El borde cambia a verde si ya se capturó, si no, es gris
            .border(1.dp, if (isCaptured) SenaGreen else colors.divider, RoundedCornerShape(12.dp))
            .background(colors.overlayBackground, RoundedCornerShape(12.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Icono central grande
            Icon(
                imageVector = Icons.Default.Fingerprint,
                contentDescription = null,
                // Lógica de colores: Verde (Capturado) -> Verde opaco (Escaneando) -> Gris (Esperando)
                tint = if (isCaptured) SenaGreen else if (isScanning) SenaGreen.copy(alpha = 0.5f) else colors.textSecondary,
                modifier = Modifier.size(60.dp).padding(bottom = 16.dp)
            )

            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ThemeText)
            Spacer(modifier = Modifier.height(16.dp))

            // Botón de interacción
            Button(
                onClick = onScanClick,
                enabled = !isScanning && !isCaptured, // Se desactiva si está escaneando o ya terminó
                modifier = Modifier.fillMaxWidth().height(48.dp).pressScale(pressedScale = 0.96f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCaptured) SenaGreen else Color.Transparent,
                    contentColor = if (isCaptured) colors.textOnPrimary else colors.textPrimary,
                    disabledContainerColor = if (isCaptured) SenaGreen else Color.Transparent,
                    disabledContentColor = if (isCaptured) colors.textOnPrimary else colors.textPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                border = if (!isCaptured) BorderStroke(1.dp, SenaGreen) else null
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Un bloque 'when' (equivalente a un switch o múltiples if/else)
                    // para cambiar el contenido del botón según el estado
                    when {
                        isScanning -> {
                            // Muestra el spinner de carga
                            CircularProgressIndicator(color = SenaGreen, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ESCANEANDO...")
                        }
                        isCaptured -> {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("VERIFICADO", fontWeight = FontWeight.Bold)
                        }
                        else -> {
                            Icon(Icons.Default.Sensors, contentDescription = null, tint = SenaGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("CAPTURAR", color = SenaGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Texto inferior informativo
            Text(
                text = when {
                    isScanning -> "Procesando biometría..."
                    isCaptured -> "Identidad confirmada"
                    else -> "Esperando captura"
                },
                fontSize = 12.sp, color = ThemeText.copy(alpha = 0.5f), modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}