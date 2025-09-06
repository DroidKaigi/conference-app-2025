package io.github.confsched.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.model.profile.ProfileCardTheme
import io.github.droidkaigi.confsched.profile.ProfileRes
import io.github.droidkaigi.confsched.profile.card_theme_selector_check
import io.github.droidkaigi.confsched.profile.card_theme_selector_logo_day
import io.github.droidkaigi.confsched.profile.card_theme_selector_logo_night
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ThemeWithShape(
    selected: Boolean,
    theme: ProfileCardTheme,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(112.dp)
            .then(
                if (selected) {
                    // fill the gap with outer border
                    Modifier.background(color = Color.White)
                } else {
                    Modifier
                },
            )
            .clickable(onClick = onSelect),
        contentAlignment = Alignment.Center,
    ) {
        CardFrontBackgroundImage(
            isDark = theme.isDark,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(2.dp)),
        )
        Box(
            Modifier
                .heightIn(max = 90.dp)
                .aspectRatio(1f)
                .background(
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = theme.shape,
                ),
        )
        Image(
            painter = painterResource(
                if (theme.isDark) {
                    ProfileRes.drawable.card_theme_selector_logo_day
                } else {
                    ProfileRes.drawable.card_theme_selector_logo_night
                },
            ),
            contentDescription = null,
            modifier = Modifier.height(30.dp),
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    // draw outer border without changing the parent size
                    .drawWithContent {
                        val strokeWidth = 3.dp.toPx()
                        val halfStorkeWidth = strokeWidth / 2

                        drawRoundRect(
                            topLeft = Offset(-halfStorkeWidth, -halfStorkeWidth),
                            size = size.copy(
                                width = size.width + strokeWidth,
                                height = size.height + strokeWidth,
                            ),
                            style = Stroke(3.dp.toPx()),
                            cornerRadius = CornerRadius(2.dp.toPx()),
                            color = Color.White,
                        )

                        val triangleSize = size.height * 48f / 112f

                        drawPath(
                            path = Path().apply {
                                moveTo(size.width - triangleSize, 0f)
                                lineTo(size.width, triangleSize)
                                lineTo(size.width, 0f)
                                lineTo(size.width - triangleSize, 0f)
                            },
                            color = Color.White,
                        )
                    },
            )
            Image(
                painter = painterResource(ProfileRes.drawable.card_theme_selector_check),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp, end = 4.dp)
                    .size(20.dp),
            )
        }
    }
}

@Preview
@Composable
private fun ThemeWithShapePreview() {
    KaigiPreviewContainer {
        ThemeWithShape(
            selected = true,
            theme = ProfileCardTheme.LightPill,
            onSelect = {},
            modifier = Modifier
                .padding(10.dp)
                .width(190.dp),
        )
    }
}
