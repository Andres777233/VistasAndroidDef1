// Pantalla de inicio de sesión: permite autenticarse con correo y contraseña,
// entrar como invitado, y navegar al registro, a la recuperación de contraseña o
// al acceso biométrico por huella. Al autenticarse devuelve el rol al MainActivity
// para redirigir al dashboard correspondiente.
// Paquete donde está este archivo
package com.example.sennaccess.ui.theme

// Importamos herramientas que vamos a usar en la interfaz
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.sennaccess.R
import com.example.sennaccess.ui.theme.LocalAppColors
import com.example.sennaccess.ui.LoginViewModel
import com.example.sennaccess.ui.LoginUiState
import com.example.sennaccess.ui.ios.GlowSpheres
import com.example.sennaccess.ui.ios.glassSurface
import com.example.sennaccess.ui.ios.GlassCornerRadiusLg
import com.example.sennaccess.ui.ios.pressScale
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode


// ESTA ES LA PANTALLA PRINCIPAL DEL LOGIN
@Composable
fun LoginScreen(

    // Funciones para navegar a otras pantallas
    onNavigateToRegister: () -> Unit,
    onNavigateToRecovery: () -> Unit,
    onNavigateToFingerprint: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    isDark: Boolean = true,
    onToggleTheme: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    val colors = LocalAppColors.current
    // Estado del ViewModel observado en la UI: maneja Loading / Error / Success.
    val uiState by viewModel.uiState.collectAsState()

    // Variables para guardar texto del correo y contraseña
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Variable para saber si estamos en modo invitado o login normal
    var isGuestMode by remember { mutableStateOf(false) }

    // Caja principal que ocupa toda la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Luces ambientales detrás del vidrio (acentúan el glassmorphism).
        GlowSpheres(isDark = isDark)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
        IconButton(
            onClick = onToggleTheme,
            modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
        ) {
            Icon(
                imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = if (isDark) "Modo claro" else "Modo oscuro",
                tint = colors.textPrimary
            )
        }

        // Tarjeta principal del login
        GlassCard {

            // Column sirve para poner elementos uno debajo del otro
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Imagen del logo SENA
                Image(
                    painter = painterResource(R.drawable.logo_sena),
                    contentDescription = "Logo SENA",
                    modifier = Modifier
                        .size(110.dp)
                        .padding(bottom = 12.dp)
                )

                // Texto "Sena Access"
                // buildAnnotatedString sirve para cambiar color a partes del texto
                Text(
                    text = buildAnnotatedString {

                        append("Sena ")

                        // Esta parte pone "Access" verde y en negrita
                        withStyle(
                            style = SpanStyle(
                                color = SenaGreen,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Access")
                        }
                    },

                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )

                // Texto pequeño debajo
                Text(
                    "Acceso CCyS",
                    fontSize = 16.sp,
                    color = SenaGreen.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Si está activado modo invitado
                if (isGuestMode) {

                    // Mostrar formulario invitado
                    GuestForm(
                        onBackToLogin = {
                            isGuestMode = false
                        }
                    )

                } else {

                    // Mostrar login normal
                    NormalLoginForm(
                        email = email,
                        password = password,

                        // Guardar cambios del correo
                        onEmailChange = {
                            email = it
                        },

                        // Guardar cambios contraseña
                        onPasswordChange = {
                            password = it
                        },

                        // Activar modo invitado
                        onGuestModeClick = {
                            isGuestMode = true
                        },

                        // Navegar pantallas
                        onRegisterClick = onNavigateToRegister,
                        onRecoveryClick = onNavigateToRecovery,
                        onFingerprintClick = onNavigateToFingerprint,
                        onLoginSuccess = onLoginSuccess,
                        viewModel = viewModel,
                        uiState = uiState
                    )
                    // --- ACCESO RÁPIDO CORREGIDO (MÁS VISIBLE) ---
                    Spacer(modifier = Modifier.height(16.dp))



                    }
                }
                }
            }
        }
        }




// ESTA FUNCIÓN CREA UNA TARJETA CON ESTILO iOS (VIDRIO ESMERILADO)
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            // ancho de la tarjeta
            .fillMaxWidth(0.95f)
            // espacio vertical
            .padding(vertical = 20.dp)
            // vidrio esmerilado iOS (blur real en API31+, translúcido abajo)
            .glassSurface(cornerRadius = GlassCornerRadiusLg, elevated = true),
        content = content
    )
}


// BOTÓN PRINCIPAL VERDE
@Composable
fun PrimaryNeonButton(

    // texto del botón
    text: String,

    // icono opcional
    icon: ImageVector? = null,

    // acción al hacer clic
    onClick: () -> Unit,

    modifier: Modifier = Modifier
) {

    Button(
        onClick = onClick,

        modifier = modifier
            .pressScale(pressedScale = 0.97f)
            .shadow(
                15.dp,
                RoundedCornerShape(12.dp),
                spotColor = SenaGreen
            ),

        colors = ButtonDefaults.buttonColors(
            containerColor = SenaGreen,
            contentColor = Color.Black
        ),

        shape = RoundedCornerShape(12.dp),

        contentPadding = PaddingValues(vertical = 14.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            // Si el botón tiene icono
            if (icon != null) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))
            }

            // Texto del botón
            Text(
                text = text.uppercase(),
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
        }
    }
}


// BOTÓN CON BORDE VERDE
@Composable
fun GlowOutlinedButton(
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.pressScale(pressedScale = 0.97f),

        // borde verde
        border = BorderStroke(
            2.dp,
            SenaGreen.copy(alpha = 0.5f)
        ),

        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = ThemeText
        ),

        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(vertical = 14.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            // mostrar icono si existe
            if (icon != null) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = SenaGreen
                )

                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = text.uppercase(),
                letterSpacing = 2.sp
            )
        }
    }
}


// FORMULARIO DE LOGIN NORMAL
@Composable
fun NormalLoginForm(

    email: String,
    password: String,

    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,

    onGuestModeClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoveryClick: () -> Unit,
    onFingerprintClick: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    viewModel: LoginViewModel,
    uiState: LoginUiState
) {

    val colors = LocalAppColors.current

    // Variable para mostrar u ocultar contraseña
    var showPassword by remember {
        mutableStateOf(false)
    }

    // Mensaje de error local (campos vacíos)
    var formError by remember { mutableStateOf<String?>(null) }

    // Indicador de carga derivado del estado del ViewModel.
    val isLoading = uiState is LoginUiState.Loading

    // Título
    Text(
        "Iniciar Sesión",
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = ThemeText
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Campo correo
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,

        label = {
            Text("Correo electrónico")
        },

        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Campo contraseña
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,

        label = {
            Text("Contraseña")
        },

        modifier = Modifier.fillMaxWidth(),
        singleLine = true,

        // ocultar o mostrar contraseña
        visualTransformation =
            if (showPassword)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),

        // icono ojito
        trailingIcon = {

            IconButton(
                onClick = {
                    showPassword = !showPassword
                }
            ) {

                Icon(
                    imageVector =
                        if (showPassword)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,

                    contentDescription = null
                )
            }
        }
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Botón ingresar
    PrimaryNeonButton(
        text = if (isLoading) "CARGANDO..." else "INGRESAR",
        icon = Icons.AutoMirrored.Filled.Login,
        onClick = {
            // Validación local: campos vacíos se reportan sin llamar a la API.
            if (email.isBlank() || password.isBlank()) {
                formError = "Ingresa correo y contraseña"
            } else {
                formError = null
                viewModel.login(email, password)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    // Indicador de progreso visible mientras se autentica contra el servidor.
    if (isLoading) {
        Spacer(modifier = Modifier.height(12.dp))
        CircularProgressIndicator(
            modifier = Modifier.size(28.dp),
            color = SenaGreen,
            strokeWidth = 3.dp
        )
    }

    // Errores (validación local o error de la API)
    val errorMsg = formError ?: (uiState as? LoginUiState.Error)?.message
    if (errorMsg != null) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = errorMsg,
            color = Color.Red,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }

    // Éxito: navega directamente a la vista según el rol devuelto por la API
    if (uiState is LoginUiState.Success) {
        val res = (uiState as LoginUiState.Success).response
        LaunchedEffect(res) {
            viewModel.reset()
            onLoginSuccess(res.role ?: "")
        }
    }

    // Texto "O"
    Text(
        "O",
        color = colors.textSecondary,
        modifier = Modifier.padding(vertical = 16.dp)
    )

    // Botón invitado
    GlowOutlinedButton(
        text = "INVITADO",
        onClick = onGuestModeClick,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(24.dp))

    TextButton(onClick = onRegisterClick) {
        Text(
            text = buildAnnotatedString {
                // Primera parte: Texto normal en gris
                withStyle(style = SpanStyle(color = colors.textSecondary)) {
                    append("¿No estás registrado? ")
                }
                // Segunda parte: Texto verde sólido y en negrita (Sin brillo)
                withStyle(style = SpanStyle(color = SenaGreen, fontWeight = FontWeight.Bold)) {
                    append("¡Regístrate aquí!")
                }
            },
            fontSize = 14.sp
        )
    }

    TextButton(onClick = onRecoveryClick) {
        Text(
            text = buildAnnotatedString {
                // Primera parte: Texto normal en gris
                withStyle(style = SpanStyle(color = colors.textSecondary)) {
                    append("¿Olvidaste tu contraseña? ")
                }
                // Segunda parte: Texto verde sólido y en negrita (Sin brillo)
                withStyle(style = SpanStyle(color = SenaGreen, fontWeight = FontWeight.Bold)) {
                    append("Recuperar")
                }
            },
            fontSize = 14.sp
        )
    }
}


// FORMULARIO INVITADO
@Composable
fun GuestForm(

    // función para volver login
    onBackToLogin: () -> Unit
) {

    // variables
    var document by remember {
        mutableStateOf("")
    }

    var name by remember {
        mutableStateOf("")
    }

    // títulos
    Text("Invitado")
    Text("Ingreso Rápido")

    // campo documento
    OutlinedTextField(
        value = document,
        onValueChange = {
            document = it
        },

        label = {
            Text("Número de Documento")
        },

        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    // campo nombre
    OutlinedTextField(
        value = name,
        onValueChange = {
            name = it
        },

        label = {
            Text("Nombre Completo")
        },

        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(32.dp))

    // botón registrar ingreso
    PrimaryNeonButton(
        text = "REGISTRAR INGRESO",
        onClick = { /* lógica de registro invitado */ },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    // botón volver
    GlowOutlinedButton(
        text = "VOLVER AL LOGIN",
        onClick = onBackToLogin,
        modifier = Modifier.fillMaxWidth()
    )
}