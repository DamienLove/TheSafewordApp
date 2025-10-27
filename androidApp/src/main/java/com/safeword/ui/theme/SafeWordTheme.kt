package com.safeword.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1F6FEB),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF123B73),
    onPrimaryContainer = Color(0xFFE6F1FF),
    secondary = Color(0xFF21D1C2),
    onSecondary = Color(0xFF04111A),
    secondaryContainer = Color(0xFF0B4B43),
    onSecondaryContainer = Color(0xFFE5FFFA),
    background = Color(0xFF07121C),
    onBackground = Color(0xFFE6F1FF),
    surface = Color(0xFF102030),
    onSurface = Color(0xFFE6F1FF),
    surfaceVariant = Color(0xFF16273C),
    onSurfaceVariant = Color(0xFF9BB0CC),
    outline = Color(0xFF30455F),
    error = Color(0xFFFF4D6D),
    onError = Color(0xFFFFFFFF)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1F6FEB),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF21D1C2),
    onSecondary = Color(0xFF04111A),
    background = Color(0xFFEFF5FF),
    onBackground = Color(0xFF04111A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF04111A)
)

@Composable
fun SafeWordTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
