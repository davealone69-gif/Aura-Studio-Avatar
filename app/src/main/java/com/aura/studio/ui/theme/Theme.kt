package com.aura.studio.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val CyberBg = Color(0xFF05070C)
val CyberSurface = Color(0xFF0B0F18)
val CyberSurfaceElevated = Color(0xFF121826)
val CyberPanel = Color(0xFF0F1524)
val CyberCyan = Color(0xFF00F0FF)
val CyberCyanDim = Color(0xFF00A8B8)
val CyberMagenta = Color(0xFFFF2BD6)
val CyberPurple = Color(0xFF7B5CFF)
val CyberText = Color(0xFFE8F0FF)
val CyberTextDim = Color(0xFF8A9BB8)
val CyberBorder = Color(0xFF1A1F2A)
val CyberGlass = Color(0x14FFFFFF)
val CyberError = Color(0xFFFF4D6A)
val CyberSuccess = Color(0xFF00E5A0)
val CyberGold = Color(0xFFFFD700)

private val CyberDarkScheme = darkColorScheme(
    primary = CyberCyan, onPrimary = CyberBg,
    secondary = CyberMagenta, onSecondary = CyberBg,
    tertiary = CyberPurple, background = CyberBg, onBackground = CyberText,
    surface = CyberSurface, onSurface = CyberText,
    surfaceVariant = CyberPanel, onSurfaceVariant = CyberTextDim,
    outline = CyberBorder, error = CyberError, onError = CyberText
)

private val CyberTypography = Typography(
    displayLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp, color = CyberText),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp, color = CyberText),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = CyberText),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, color = CyberText),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, color = CyberText),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, color = CyberTextDim),
    labelLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CyberCyan),
    labelSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 11.sp, color = CyberCyanDim, letterSpacing = 1.sp)
)

@Composable
fun CyberBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(CyberBg)
            .background(Brush.radialGradient(listOf(CyberCyan.copy(0.07f), Color.Transparent), Offset(0f, 0f), 900f))
            .background(Brush.radialGradient(listOf(CyberMagenta.copy(0.05f), Color.Transparent), Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY), 800f))
    ) { content() }
}

@Composable
fun AuraTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = CyberDarkScheme, typography = CyberTypography, content = content)
}
