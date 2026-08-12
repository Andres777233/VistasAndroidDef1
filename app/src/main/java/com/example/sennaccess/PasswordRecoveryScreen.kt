// Pantalla de recuperación de contraseña: permite solicitar el envío de un código
// de recuperación al correo institucional. El envío aún es un placeholder (onClick
// vacío pendiente de conectar al backend); desde aquí se regresa al login con
// onBackToLogin.
// Paquete donde está este archivo
package com.example.sennaccess.ui.theme

// Importamos herramientas necesarias para la interfaz
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.res.painterResource
import com.example.sennaccess.R
import com.example.sennaccess.ui.theme.LocalAppColors
import com.example.sennaccess.ui.theme.WarningYellow
import com.example.sennaccess.ui.ios.GlowSpheres
import com.example.sennaccess.ui.ios.glassSurface
import com.example.sennaccess.ui.ios.GlassCornerRadiusLg
import com.example.sennaccess.ui.ios.pressScale
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode


// ESTA ES LA PANTALLA DE RECUPERAR CONTRASEÑA
// Composable principal: formulario de recuperación dentro de una tarjeta de cristal.
@Composable
fun PasswordRecoveryScreen(
    // Función para volver al login
    onBackToLogin: () -> Unit,
    isDark: Boolean = true,
    onToggleTheme: () -> Unit = {}
) {
    val colors = LocalAppColors.current

    // Variable para guardar el correo
    var email by remember {
        mutableStateOf("")
    }

    // Caja principal que ocupa toda la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()

            // color de fondo oscuro
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

        // Imagen de fondo (patrón)
        Image(

            // Cargar imagen desde internet
            painter = rememberAsyncImagePainter(
                "https://www.sena.edu.co/Style%20Library/alayout/images/pattern.png"
            ),

            contentDescription = null,

            modifier = Modifier
                .fillMaxSize()

                // transparencia para que no se vea muy fuerte
                .graphicsLayer(alpha = 0.12f),

            // hace que la imagen cubra toda la pantalla
            contentScale = ContentScale.Crop
        )

        // Luces ambientales detrás del vidrio
        GlowSpheres(isDark = isDark)

        // Caja principal centrada
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

            // centrar contenido
            contentAlignment = Alignment.Center
        ) {

            // TARJETA CENTRAL (vidrio esmerilado iOS)
            Box(
                modifier = Modifier

                    // ancho de la tarjeta
                    .fillMaxWidth(0.95f)

                    // vidrio esmerilado iOS
                    .glassSurface(cornerRadius = GlassCornerRadiusLg, elevated = true)

                    // espacio interno
                    .padding(
                        horizontal = 24.dp,
                        vertical = 40.dp
                    ),

                contentAlignment = Alignment.Center
            ) {

                // Column organiza elementos verticalmente
                Column(

                    modifier = Modifier.fillMaxWidth(),

                    // centra elementos horizontalmente
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Texto grande SENA
                    Text(
                        "SENA",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colors.textPrimary
                    )

                    // Texto pequeño debajo
                    Text(
                        "Bienvenido al CCyS",
                        fontSize = 16.sp,

                        // copia color pero con transparencia
                        color = ThemeText.copy(alpha = 0.8f),

                        modifier = Modifier.padding(bottom = 16.dp)
                    )


                    // Logo del sena
                    Image(
                        painter = rememberAsyncImagePainter(
                            "https://www.sena.edu.co/Style%20Library/alayout/images/logoSena.png?rev=40"
                        ),

                        contentDescription = "Logo SENA",

                        modifier = Modifier
                            .size(90.dp)
                            .padding(bottom = 16.dp)
                    )


                    // Título de recuperar contraseña
                    Text(
                        text = "Recuperar Contraseña",

                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,

                        // color amarillo
                        color = WarningYellow,

                        modifier = Modifier.padding(bottom = 32.dp)
                    )


                    // INPUT DEL CORREO
                    OutlinedTextField(

                        // valor escrito
                        value = email,

                        // guardar cambios del texto
                        onValueChange = {
                            email = it
                        },

                        // texto del input
                        label = {
                            Text(
                                "Correo Electrónico",
                                color = colors.textSecondary
                            )
                        },

                        modifier = Modifier.fillMaxWidth(),

                        // solo una línea
                        singleLine = true,

                        // colores personalizados
                        colors = OutlinedTextFieldDefaults.colors(

                            // borde cuando está seleccionado
                            focusedBorderColor = SenaGreen,

                            // borde cuando no está seleccionado
                            unfocusedBorderColor =
                                colors.divider,

                            // color del texto
                            focusedTextColor = colors.textPrimary,
                            unfocusedTextColor = colors.textPrimary,

                            // fondo del input
                            focusedContainerColor =
                                colors.surfaceVariant.copy(alpha = 0.5f),

                            unfocusedContainerColor =
                                colors.surfaceVariant.copy(alpha = 0.5f)
                        )
                    )


                    Spacer(
                        modifier = Modifier.height(32.dp)
                    )


                    // BOTÓN ENVIAR CÓDIGO
                    Button(

                        // acción del botón
                        onClick = {
                            // Pendiente: aquí se conectará el envío real del código al backend.
                            /* lógica envío */
                        },

                        modifier = Modifier
                            .fillMaxWidth()

                            // efecto táctil iOS
                            .pressScale(pressedScale = 0.97f)

                            // sombra del botón
                            .shadow(
                                15.dp,
                                RoundedCornerShape(12.dp),
                                spotColor = SenaGreen
                            ),

                        // colores botón
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SenaGreen,
                            contentColor = Color.Black
                        ),

                        // esquinas redondeadas
                        shape = RoundedCornerShape(12.dp),

                        contentPadding = PaddingValues(
                            vertical = 14.dp
                        )
                    ) {

                        // Row organiza horizontalmente
                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            // icono enviar
                            Icon(
                                imageVector =
                                    Icons.AutoMirrored.Filled.Send,

                                contentDescription = null,

                                modifier =
                                    Modifier.size(20.dp)
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(8.dp)
                            )

                            // texto botón
                            Text(
                                text = "ENVIAR CÓDIGO",

                                fontWeight =
                                    FontWeight.ExtraBold,

                                letterSpacing = 2.sp
                            )
                        }
                    }


                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )


                    // BOTÓN TEXTO VOLVER AL LOGIN
                    TextButton(

                        // volver al login
                        onClick = onBackToLogin
                    ) {

                        // texto con color personalizado
                        Text(
                            text = buildAnnotatedString {

                                append(
                                    "¿Recordaste tu contraseña? "
                                )

                                // cambiar color solo a esta parte
                                withStyle(
                                    style = SpanStyle(
                                        color = SenaGreen,
                                        fontWeight =
                                            FontWeight.Bold
                                    )
                                ) {
                                    append("Inicia sesión")
                                }
                            },

                            color =
                                ThemeText.copy(alpha = 0.8f),

                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}