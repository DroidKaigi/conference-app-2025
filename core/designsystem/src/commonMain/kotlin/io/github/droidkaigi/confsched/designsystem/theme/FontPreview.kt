package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview(heightDp = 1400)
internal fun FontPreview() {
    Column(
        Modifier.background(color = Color.White),
    ) {
        val fonts = listOf(
            changoFontFamily(),
            robotoMediumFontFamily(),
            robotoRegularFontFamily(),
            // Add here when adding fonts.
        )
        fonts.forEach { fontFamily ->
            Text("Display Large", style = AppTypography(fontFamily).displayLarge)
            Text("Display Medium", style = AppTypography(fontFamily).displayMedium)
            Text("Display Small", style = AppTypography(fontFamily).displaySmall)
            Text("Headline Large", style = AppTypography(fontFamily).headlineLarge)
            Text("Headline Medium", style = AppTypography(fontFamily).headlineMedium)
            Text("Headline Small", style = AppTypography(fontFamily).headlineSmall)
            Text("Title Large", style = AppTypography(fontFamily).titleLarge)
            Text("Title Medium", style = AppTypography(fontFamily).titleMedium)
            Text("Title Small", style = AppTypography(fontFamily).titleSmall)
            Text("Label Large", style = AppTypography(fontFamily).labelLarge)
            Text("Label Medium", style = AppTypography(fontFamily).labelMedium)
            Text("Label Small", style = AppTypography(fontFamily).labelSmall)
            Text("Body Large", style = AppTypography(fontFamily).bodyLarge)
            Text("Body Medium", style = AppTypography(fontFamily).bodyMedium)
            Text("Body Small", style = AppTypography(fontFamily).bodySmall)
        }
    }
}
