package com.aura.studio.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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

private val CyberDarkScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = CyberBg,
    secondary = CyberMagenta,
    onSecondary = CyberBg,
    tertiary = CyberPurple,
    background = CyberBg,
    onBackground = CyberText,
    surface = CyberSurface,
    onSurface = CyberText,
    surfaceVariant = CyberPanel,
    onSurfaceVariant = CyberTextDim,
    outline = CyberBorder,
    error = CyberError,
    onError = CyberText
)

@Composable
fun AuraTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CyberDarkScheme,
        content = content
    )
}
