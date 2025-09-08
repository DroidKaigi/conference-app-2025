package io.github.droidkaigi.confsched.about.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction.Cancel
import androidx.compose.foundation.interaction.PressInteraction.Press
import androidx.compose.foundation.interaction.PressInteraction.Release
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.icon_youtube
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AboutLinkIcon(
    testTag: String,
    painter: Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val animationScale by remember { mutableStateOf(Animatable(1f)) }
    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            when (it) {
                is Press -> animationScale.animateTo(targetValue = 0.9f)
                is Release, is Cancel -> animationScale.animateTo(targetValue = 1.0f)
            }
        }
    }

    IconButton(
        onClick = onClick,
        shapes = IconButtonDefaults.shapes(),
        interactionSource = interactionSource,
        modifier = modifier
            .testTag(testTag),
    ) {
        Image(
            painter = painter,
            modifier = Modifier.size(48.dp),
            contentDescription = contentDescription,
        )
    }
}

@Preview
@Composable
private fun AboutLinkIconPreview() {
    KaigiPreviewContainer {
        AboutLinkIcon(
            testTag = "testTag",
            painter = painterResource(AboutRes.drawable.icon_youtube),
            contentDescription = "YouTube",
            onClick = {},
        )
    }
}
