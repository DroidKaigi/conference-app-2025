package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import io.github.droidkaigi.confsched.designsystem.Chango_Regular
import io.github.droidkaigi.confsched.designsystem.DesignsystemRes
import io.github.droidkaigi.confsched.designsystem.Roboto_Medium
import io.github.droidkaigi.confsched.designsystem.Roboto_Regular
import org.jetbrains.compose.resources.Font

@Composable
actual fun changoFontFamily(): FontFamily = FontFamily(
    Font(DesignsystemRes.font.Chango_Regular, FontWeight.Normal),
)

@Composable
actual fun robotoRegularFontFamily(): FontFamily = FontFamily(
    Font(DesignsystemRes.font.Roboto_Regular, FontWeight.Normal),
)

@Composable
actual fun robotoMediumFontFamily(): FontFamily = FontFamily(
    Font(DesignsystemRes.font.Roboto_Medium, FontWeight.Medium),
)
