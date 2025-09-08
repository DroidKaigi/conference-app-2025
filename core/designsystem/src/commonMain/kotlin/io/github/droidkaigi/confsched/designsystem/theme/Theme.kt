package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

private val fixedAccentColors = FixedAccentColors(
    primaryFixed = Color(0xFFD8E2FF),
    onPrimaryFixed = Color(0xFF001A43),
    onPrimaryFixedVariant = Color(0xFF2D4678),
    secondaryFixed = Color(0xFFE8DEFF),
    onSecondaryFixed = Color(0xFF1F1048),
    onSecondaryFixedVariant = Color(0xFF4B3E76),
    tertiaryFixed = Color(0xFFDBE1FF),
    onTertiaryFixed = Color(0xFF00174B),
    onTertiaryFixedVariant = Color(0xFF334478),
    primaryFixedDim = Color(0xFFAEC6FF),
    secondaryFixedDim = Color(0xFFCDBDFF),
    tertiaryFixedDim = Color(0xFFB4C5FF),
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    surfaceTint = surfaceTintDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
    primaryFixed = fixedAccentColors.primaryFixed,
    onPrimaryFixed = fixedAccentColors.onPrimaryFixed,
    onPrimaryFixedVariant = fixedAccentColors.onPrimaryFixedVariant,
    secondaryFixed = fixedAccentColors.secondaryFixed,
    onSecondaryFixed = fixedAccentColors.onSecondaryFixed,
    onSecondaryFixedVariant = fixedAccentColors.onSecondaryFixedVariant,
    tertiaryFixed = fixedAccentColors.tertiaryFixed,
    onTertiaryFixed = fixedAccentColors.onTertiaryFixed,
    onTertiaryFixedVariant = fixedAccentColors.onTertiaryFixedVariant,
    primaryFixedDim = fixedAccentColors.primaryFixedDim,
    secondaryFixedDim = fixedAccentColors.secondaryFixedDim,
    tertiaryFixedDim = fixedAccentColors.tertiaryFixedDim,
)

data class FixedAccentColors(
    val primaryFixed: Color,
    val onPrimaryFixed: Color,
    val onPrimaryFixedVariant: Color,
    val secondaryFixed: Color,
    val onSecondaryFixed: Color,
    val onSecondaryFixedVariant: Color,
    val tertiaryFixed: Color,
    val onTertiaryFixed: Color,
    val onTertiaryFixedVariant: Color,
    val primaryFixedDim: Color,
    val secondaryFixedDim: Color,
    val tertiaryFixedDim: Color,
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun KaigiTheme(
    fontFamily: FontFamily? = null,
    content: @Composable () -> Unit,
) {
    MaterialExpressiveTheme(
        // currently, we do not support light theme
        colorScheme = darkScheme,
        typography = AppTypography(fontFamily),
        content = content,
    )
}
