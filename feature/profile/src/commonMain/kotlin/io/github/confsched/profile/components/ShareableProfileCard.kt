package io.github.confsched.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.model.profile.ProfileCardTheme
import io.github.droidkaigi.confsched.profile.ProfileRes
import io.github.droidkaigi.confsched.profile.shareable_card_background_day
import io.github.droidkaigi.confsched.profile.shareable_card_background_night
import io.github.droidkaigi.confsched.profile.shareable_card_background_star_orbit
import io.github.droidkaigi.confsched.profile.shareable_card_foreground_star_orbit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ShareableProfileCard(
    theme: ProfileCardTheme,
    profileImageBitmap: ImageBitmap,
    qrImageBitmap: ImageBitmap,
    nickName: String,
    occupation: String,
    onRenderResultUpdate: (ImageBitmap) -> Unit,
) {
    var frontCardImageBitmap by remember(theme, nickName) { mutableStateOf<ImageBitmap?>(null) }
    var backCardImageBitmap by remember(theme, qrImageBitmap) { mutableStateOf<ImageBitmap?>(null) }

    CompositionLocalProvider(
        LocalDensity provides Density(
            density = 1f,
            fontScale = 1f,
        ),
    ) {
        CaptureContainer(
            onRendered = { frontCardImageBitmap = it },
        ) {
            ProfileCardFront(
                theme = theme,
                nickName = nickName,
                occupation = occupation,
                profileImageBitmap = profileImageBitmap,
            )
        }

        CaptureContainer(
            onRendered = { backCardImageBitmap = it },
        ) {
            ProfileCardBack(
                theme = theme,
                qrImageBitmap = qrImageBitmap,
            )
        }

        CaptureContainer(
            onRendered = {
                onRenderResultUpdate(it)
            },
        ) {
            frontCardImageBitmap?.let { frontCardImageBitmap ->
                backCardImageBitmap?.let { backCardImageBitmap ->
                    ShareableProfileCard(
                        theme = theme,
                        frontCardImageBitmap = frontCardImageBitmap,
                        backCardImageBitmap = backCardImageBitmap,
                    )
                }
            }
        }
    }
}

@Composable
private fun ShareableProfileCard(
    theme: ProfileCardTheme,
    frontCardImageBitmap: ImageBitmap,
    backCardImageBitmap: ImageBitmap,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .requiredSize(
                width = with(density) { 1200.toDp() },
                height = with(density) { 630.toDp() },
            ),
    ) {
        Image(
            painter = painterResource(
                if (theme.isDark) {
                    ProfileRes.drawable.shareable_card_background_day
                } else {
                    ProfileRes.drawable.shareable_card_background_night
                },
            ),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
        )
        Image(
            painter = painterResource(ProfileRes.drawable.shareable_card_background_star_orbit),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
        )
        Image(
            bitmap = frontCardImageBitmap,
            contentDescription = null,
            modifier = Modifier
                .offset(
                    x = with(density) { 280.toDp() },
                    y = with(density) { 60.toDp() },
                )
                .size(
                    width = with(density) { 300.toDp() },
                    height = with(density) { 380.toDp() },
                ),
        )
        Image(
            bitmap = backCardImageBitmap,
            contentDescription = null,
            modifier = Modifier
                .offset(
                    x = with(density) { 620.toDp() },
                    y = with(density) { 190.toDp() },
                )
                .size(
                    width = with(density) { 300.toDp() },
                    height = with(density) { 380.toDp() },
                ),
        )
        Image(
            painter = painterResource(ProfileRes.drawable.shareable_card_foreground_star_orbit),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
        )
    }
}

@Preview
@Composable
private fun ShareableProfileCardPreview() {
    KaigiPreviewContainer {
        ShareableProfileCard(
            theme = ProfileCardTheme.DarkPill,
            frontCardImageBitmap = CardPreviewImageBitmaps.frontCardImage,
            backCardImageBitmap = CardPreviewImageBitmaps.backCardImage,
        )
    }
}
