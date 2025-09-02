package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Typeface
import org.jetbrains.skia.FontMgr
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.Typeface

@Composable
actual fun changoFontFamily(): FontFamily = fontFamilyFor(
    familyName = "Chango",
    fontWeight = 400,
)

@Composable
actual fun robotoRegularFontFamily(): FontFamily = fontFamilyFor(
    familyName = "Roboto",
    fontWeight = 500,
)

@Composable
actual fun robotoMediumFontFamily(): FontFamily = fontFamilyFor(
    familyName = "Roboto",
    fontWeight = 500,
)

private fun fontFamilyFor(
    familyName: String,
    fontWeight: Int = 400,
): FontFamily {
    val skTypeface = resolveTypeface(
        familyName = familyName,
        fontWeight = fontWeight,
    )
    return try {
        FontFamily(Typeface(skTypeface))
    } finally {
        skTypeface.close()
    }
}

private fun resolveTypeface(
    familyName: String,
    fontWeight: Int = 400,
): Typeface {
    val normal = FontStyle.NORMAL
    val style = FontStyle(fontWeight, normal.width, normal.slant)
    val fontMgr = FontMgr.default

    return fontMgr.matchFamilyStyle(familyName, style)
        ?: fontMgr.matchFamilyStyle(null, style) // default system font.
        ?: Typeface.makeEmpty()
}
