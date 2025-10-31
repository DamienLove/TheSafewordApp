package com.safeword.ui.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object NewUiColors {
    val background = Color(0xFF101014)
    val surfaceElevated = Color(0xFF1A1B21)
    val surfaceVariant = Color(0xFF2A2B33)
    val primary = Color(0xFF01D3C5)
    val primaryVariant = Color(0xFF009DA3)
    val accentRed = Color(0xFFFF4B55)
    val accentGold = Color(0xFFFFB032)
    val onBackground = Color(0xFFF5F6F9)
    val onSurfaceMuted = Color(0xFFB5BCC9)
    val success = Color(0xFF2BD964)
    val warning = Color(0xFFFF8035)
}

object NewUiGradients {
    val emblem = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF00E0C7),
            Color(0xFF3A7BFF)
        )
    )
}

object NewUiDimensions {
    val screenPadding: Dp = 20.dp
    val cardRadius: Dp = 24.dp
    val chipSpacing: Dp = 12.dp
    val sectionSpacing: Dp = 24.dp
    val iconButtonSize: Dp = 56.dp
    val iconSizeLarge: Dp = 32.dp
}

object NewUiTypography {
    val headingXL = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp
    )
    val headingLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    )
    val bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp
    )
    val bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
    val label = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
}

private val NewUiColorScheme = darkColorScheme(
    primary = NewUiColors.primary,
    onPrimary = Color.Black,
    primaryContainer = NewUiColors.primaryVariant,
    onPrimaryContainer = Color.White,
    secondary = NewUiColors.accentGold,
    onSecondary = Color.Black,
    background = NewUiColors.background,
    onBackground = NewUiColors.onBackground,
    surface = NewUiColors.surfaceElevated,
    onSurface = NewUiColors.onBackground,
    surfaceVariant = NewUiColors.surfaceVariant,
    onSurfaceVariant = NewUiColors.onSurfaceMuted,
    error = NewUiColors.accentRed,
    onError = Color.White,
    outline = NewUiColors.onSurfaceMuted
)

@Composable
fun NewUiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NewUiColorScheme,
        typography = MaterialTheme.typography.copy(
            headlineLarge = NewUiTypography.headingLarge,
            displaySmall = NewUiTypography.headingXL,
            titleLarge = NewUiTypography.bodyLarge,
            bodyLarge = NewUiTypography.bodyLarge,
            bodyMedium = NewUiTypography.bodyMedium,
            labelLarge = NewUiTypography.label
        ),
        content = content
    )
}
