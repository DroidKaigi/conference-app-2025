package io.github.droidkaigi.confsched.about.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.about_header
import io.github.droidkaigi.confsched.about.about_header_bg_only
import io.github.droidkaigi.confsched.about.about_header_mascot1
import io.github.droidkaigi.confsched.about.about_header_mascot2
import io.github.droidkaigi.confsched.about.about_header_mascot3
import io.github.droidkaigi.confsched.about.about_header_title_bg_title
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import org.jetbrains.compose.resources.painterResource

@Composable
fun AboutHeaderAnimatedBanner(
    modifier: Modifier = Modifier,
) {
    val titleAlpha = remember { Animatable(0f) }
    val mascot1Alpha = remember { Animatable(0f) }
    val mascot2Alpha = remember { Animatable(0f) }
    val finalAlpha = remember { Animatable(0f) }

    val density = LocalDensity.current

    val backgroundPainter = painterResource(AboutRes.drawable.about_header_bg_only)

    val dynamicAspect = remember(backgroundPainter) {
        val s = backgroundPainter.intrinsicSize
        val hasSize = s != Size.Unspecified && s.width > 0f && s.height > 0f
        if (hasSize) s.width / s.height else 1648f / 550f
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(dynamicAspect)
            .clipToBounds(),
    ) {
        val maxHeightPx = with(density) { maxHeight.toPx() }
        val mascot3OffsetY = remember(maxHeightPx) { Animatable(maxHeightPx) }

        LaunchedEffect(maxHeight) {
            playHeaderIntro(
                titleAlpha = titleAlpha,
                mascot1Alpha = mascot1Alpha,
                mascot2Alpha = mascot2Alpha,
                mascot3OffsetY = mascot3OffsetY,
                finalAlpha = finalAlpha,
            )
        }

        Image(
            painter = backgroundPainter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .zIndex(0f),
        )
        Image(
            painter = painterResource(AboutRes.drawable.about_header_mascot3),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { translationY = mascot3OffsetY.value }
                .zIndex(1.5f),
        )
        Image(
            painter = painterResource(AboutRes.drawable.about_header_title_bg_title),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = titleAlpha.value }
                .zIndex(1f),
        )
        Image(
            painter = painterResource(AboutRes.drawable.about_header_mascot1),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = mascot1Alpha.value }
                .zIndex(2.0f),
        )
        Image(
            painter = painterResource(AboutRes.drawable.about_header_mascot2),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = mascot2Alpha.value }
                .zIndex(2.1f),
        )
        Image(
            painter = painterResource(AboutRes.drawable.about_header),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = finalAlpha.value }
                .zIndex(2.2f),
        )
    }
}

private suspend fun playHeaderIntro(
    titleAlpha: Animatable<Float, *>,
    mascot1Alpha: Animatable<Float, *>,
    mascot2Alpha: Animatable<Float, *>,
    mascot3OffsetY: Animatable<Float, *>,
    finalAlpha: Animatable<Float, *>,
) {
    delay(180)

    titleAlpha.animateTo(1f, tween(durationMillis = 800))
    delay(80)

    coroutineScope {
        val jobMascot1 = launch {
            mascot1Alpha.animateTo(1f, tween(durationMillis = 500))
        }
        val jobMascot2 = launch {
            while (mascot1Alpha.value < 0.5f) {
                yield()
                delay(16)
            }
            mascot2Alpha.animateTo(1f, tween(durationMillis = 500))
        }
        jobMascot1.join()
        jobMascot2.join()
    }

    delay(40)
    mascot3OffsetY.animateTo(
        targetValue = 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
    )
    delay(80)

    finalAlpha.animateTo(1f, tween(durationMillis = 500))
}
