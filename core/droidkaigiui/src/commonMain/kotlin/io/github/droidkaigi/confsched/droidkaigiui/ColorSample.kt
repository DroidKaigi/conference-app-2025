package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
private fun ColorBoxes() {
    Column {
        Row {
            Column {
                ColorBox(MaterialTheme.colorScheme.primary)
                ColorBox(MaterialTheme.colorScheme.onPrimary)
                ColorBox(MaterialTheme.colorScheme.primaryContainer)
                ColorBox(MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Column {
                ColorBox(MaterialTheme.colorScheme.secondary)
                ColorBox(MaterialTheme.colorScheme.onSecondary)
                ColorBox(MaterialTheme.colorScheme.secondaryContainer)
                ColorBox(MaterialTheme.colorScheme.onSecondaryContainer)
            }
            Column {
                ColorBox(MaterialTheme.colorScheme.tertiary)
                ColorBox(MaterialTheme.colorScheme.onTertiary)
                ColorBox(MaterialTheme.colorScheme.tertiaryContainer)
                ColorBox(MaterialTheme.colorScheme.onTertiaryContainer)
            }
            Column {
                ColorBox(MaterialTheme.colorScheme.error)
                ColorBox(MaterialTheme.colorScheme.onError)
                ColorBox(MaterialTheme.colorScheme.errorContainer)
                ColorBox(MaterialTheme.colorScheme.onErrorContainer)
            }
        }
        Row {
            Row {
                Column {
                    Row {
                        ColorBox(MaterialTheme.colorScheme.primaryFixed)
                        ColorBox(MaterialTheme.colorScheme.primaryFixedDim)
                    }
                    ColorBox(MaterialTheme.colorScheme.onPrimaryFixed)
                    ColorBox(MaterialTheme.colorScheme.onPrimaryFixedVariant)
                }
            }
            Row {
                Column {
                    Row {
                        ColorBox(MaterialTheme.colorScheme.secondaryFixed)
                        ColorBox(MaterialTheme.colorScheme.secondaryFixedDim)
                    }
                    ColorBox(MaterialTheme.colorScheme.onSecondaryFixed)
                    ColorBox(MaterialTheme.colorScheme.onSecondaryFixedVariant)
                }
            }
            Row {
                Column {
                    Row {
                        ColorBox(MaterialTheme.colorScheme.tertiaryFixed)
                        ColorBox(MaterialTheme.colorScheme.tertiaryFixedDim)
                    }
                    ColorBox(MaterialTheme.colorScheme.onTertiaryFixed)
                    ColorBox(MaterialTheme.colorScheme.onTertiaryFixedVariant)
                }
            }
        }
        Row {
            Column {
                Row {
                    ColorBox(MaterialTheme.colorScheme.surface)
                    ColorBox(MaterialTheme.colorScheme.surfaceDim)
                    ColorBox(MaterialTheme.colorScheme.surfaceBright)
                }
                Row {
                    ColorBox(MaterialTheme.colorScheme.surfaceContainerLowest)
                    ColorBox(MaterialTheme.colorScheme.surfaceContainerLow)
                    ColorBox(MaterialTheme.colorScheme.surfaceContainer)
                    ColorBox(MaterialTheme.colorScheme.surfaceContainerHigh)
                    ColorBox(MaterialTheme.colorScheme.surfaceContainerHighest)
                }
                Row {
                    ColorBox(MaterialTheme.colorScheme.onSurface)
                    ColorBox(MaterialTheme.colorScheme.onSurfaceVariant)
                    ColorBox(MaterialTheme.colorScheme.outline)
                    ColorBox(MaterialTheme.colorScheme.outlineVariant)
                }
                Row {
                    ColorBox(MaterialTheme.colorScheme.scrim)
                }
            }
            Column {
                ColorBox(MaterialTheme.colorScheme.inverseSurface)
                ColorBox(MaterialTheme.colorScheme.inverseOnSurface)
                ColorBox(MaterialTheme.colorScheme.inversePrimary)
            }
        }
    }
}

@Composable
private fun ColorBox(color: Color) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(color),
    )
}

@Preview
@Composable
private fun ColorPreview_Light() {
    KaigiTheme(
        darkTheme = false,
    ) {
        Surface {
            ColorBoxes()
        }
    }
}

@Preview
@Composable
private fun ColorPreview_Dark() {
    KaigiTheme(
        darkTheme = true,
    ) {
        Surface {
            ColorBoxes()
        }
    }
}
